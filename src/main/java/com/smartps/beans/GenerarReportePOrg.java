package com.smartps.beans;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

import com.smartps.dao.PSDao;
import com.smartps.dao.PlanDeTrabajoDao;
import com.smartps.dao.OrganizacionDao;
import com.smartps.model.PS;
import com.smartps.model.PlanDeTrabajo;
import com.smartps.model.Organizacion;
import com.smartps.model.LineaDeReporte;
import com.smartps.model.LineaPorcentaje;

@ManagedBean
@RequestScoped
public class GenerarReportePOrg {
//GenerarReportePOrg = GenerarReportePlanesPorOrganizacion_hdu23
	
	private PSDao psdao = PSDao.getInstance();
	private PlanDeTrabajoDao ptdao = PlanDeTrabajoDao.getInstance();
	private OrganizacionDao orgdao = OrganizacionDao.getInstance();

	private List<PS> pslist;
	private List<PS> pslistCL;
	private List<PS> pslistCuatri;
	private List<PlanDeTrabajo> planes;
	private List<PlanDeTrabajo> planesPeriodo;	
	private List<LineaDeReporte> linealist;
	private List<LineaDeReporte> resultlist;
	private List<LineaPorcentaje> auxporclist;
	private List<LineaPorcentaje> porclist;

	
	private List<Organizacion> orglist;
	private String org;
	private Map<String,String> orgs = new HashMap<String, String>();
	
	private int cicloLectivo;
	private int cuatrimestre;
	private Date desde;
	private Date hasta;
	private LineaDeReporte linea;	
	private LineaPorcentaje linPorc;

	private boolean totOrg;
	private boolean totTodos;
	
	//contadores
	private double cPP;
	private double cPPre;
	private double cPA;
	private double cPO;
	private double cPR;
	private double cPV;
	
	
	@PostConstruct
	public void init(){
		pslist = new ArrayList<PS>();
		pslistCL = new ArrayList<PS>();
		pslistCuatri = new ArrayList<PS>();
		planes = new ArrayList<PlanDeTrabajo>();
		planesPeriodo = new ArrayList<PlanDeTrabajo>();
		linealist = new ArrayList<LineaDeReporte>();
		resultlist = new ArrayList<LineaDeReporte>();		
		auxporclist = new ArrayList<LineaPorcentaje>();
		porclist = new ArrayList<LineaPorcentaje>();
		
		orglist = new ArrayList<Organizacion>();
		orglist = orgdao.getAll();
		
		for (int v=0; v<orglist.size(); v++){
			orgs.put(orglist.get(v).getNombre(), orglist.get(v).getNombre());
		}

	}
	
	public String busquedaByFiltros(){

	//Listado		

		planes = ptdao.getAll();
		pslist = psdao.getAll();

		//Filtros
		
		if (org.isEmpty()){
			//Ningun campo rellenado
			if ((desde==null) && (hasta==null) && (cuatrimestre==0) && (cicloLectivo==0)){
				for (int a=0; a<planes.size(); a++){
					for (int b=0; b<pslist.size(); b++){
						if (planes.get(a).getPs().getId()==pslist.get(b).getId()){
							linea = new LineaDeReporte();
							linea.setFechaDePresentacion(planes.get(a).getFechaDePresentacion());
							linea.setTitulo(pslist.get(b).getTitulo());
							linea.setEstado(pslist.get(b).getEstado().getNombre());
							linea.setArea(pslist.get(b).getOrganizacion().getNombre());
							linea.setTipoActividad(pslist.get(b).getTipoActividad().getNombre());
							linea.setAlumno(pslist.get(b).getAlumno().getNombre());
							linea.setIngreso(pslist.get(b).getAlumno().getCicloLectivo());
							linealist.add(linea);
						}
					}
				}
			} else {
				//Sin Org, con periodo
				if ((desde!=null) && (hasta!=null)){
					planesPeriodo = ptdao.findByPeriodo(desde, hasta);
					for (int a=0; a<planesPeriodo.size(); a++){
						for (int b=0; b<pslist.size(); b++){
							if (planesPeriodo.get(a).getPs().getId()==pslist.get(b).getId()){
								if (
										((pslist.get(b).getCuatrimestre()==cuatrimestre) || (cuatrimestre==0))
										&&
										((pslist.get(b).getCicloLectivo()==cicloLectivo) || (cicloLectivo==0))
									){
									linea = new LineaDeReporte();
									linea.setFechaDePresentacion(planesPeriodo.get(a).getFechaDePresentacion());
									linea.setTitulo(pslist.get(b).getTitulo());
									linea.setEstado(pslist.get(b).getEstado().getNombre());
									linea.setArea(pslist.get(b).getOrganizacion().getNombre());
									linea.setTipoActividad(pslist.get(b).getTipoActividad().getNombre());
									linea.setAlumno(pslist.get(b).getAlumno().getNombre());
									linea.setIngreso(pslist.get(b).getAlumno().getCicloLectivo());
									linealist.add(linea);
								}
							}
						}
					}
				} else {
					//Sin Org, Por CicloLectivo
					pslistCL = psdao.findByCicloLectivo(cicloLectivo);
					for (int i=0; i<pslistCL.size(); i++){			
						for (int j=0; j<planes.size(); j++){
							if (pslistCL.get(i).getId()==planes.get(j).getPs().getId()){
								if ((pslistCL.get(i).getCuatrimestre()==cuatrimestre) || (cuatrimestre==0)){
									linea = new LineaDeReporte();
									linea.setFechaDePresentacion(planes.get(j).getFechaDePresentacion());
									linea.setTitulo(pslistCL.get(i).getTitulo());
									linea.setEstado(pslistCL.get(i).getEstado().getNombre());
									linea.setArea(pslistCL.get(i).getOrganizacion().getNombre());
									linea.setTipoActividad(pslistCL.get(i).getTipoActividad().getNombre());
									linea.setAlumno(pslistCL.get(i).getAlumno().getNombre());
									linea.setIngreso(pslistCL.get(i).getAlumno().getCicloLectivo());
									linealist.add(linea);
								}
							}
						}
					}
					if (cicloLectivo==0){
						//Sin Org, Por Cuatrimestre
						pslistCuatri = psdao.findByCuatrimestre(cuatrimestre);
						for (int x=0; x<pslistCuatri.size(); x++){			
							for (int y=0; y<planes.size(); y++){
								if (pslistCuatri.get(x).getId()==planes.get(y).getPs().getId()){
									linea = new LineaDeReporte();
									linea.setFechaDePresentacion(planes.get(y).getFechaDePresentacion());
									linea.setTitulo(pslistCuatri.get(x).getTitulo());
									linea.setEstado(pslistCuatri.get(x).getEstado().getNombre());
									linea.setArea(pslistCuatri.get(x).getOrganizacion().getNombre());
									linea.setTipoActividad(pslistCuatri.get(x).getTipoActividad().getNombre());
									linea.setAlumno(pslistCuatri.get(x).getAlumno().getNombre());
									linea.setIngreso(pslistCuatri.get(x).getAlumno().getCicloLectivo());
									linealist.add(linea);
								}
							}
						}
					}
				}
			}
		} else {
			//Solo con Org, los demas null
			if ((desde==null) && (hasta==null) && (cuatrimestre==0) && (cicloLectivo==0)){
				for (int a=0; a<planes.size(); a++){
					for (int b=0; b<pslist.size(); b++){
						if (planes.get(a).getPs().getId()==pslist.get(b).getId()){
							if (pslist.get(b).getOrganizacion().getNombre().equals(org)){
								linea = new LineaDeReporte();
								linea.setFechaDePresentacion(planes.get(a).getFechaDePresentacion());
								linea.setTitulo(pslist.get(b).getTitulo());
								linea.setEstado(pslist.get(b).getEstado().getNombre());
								linea.setArea(pslist.get(b).getOrganizacion().getNombre());
								linea.setTipoActividad(pslist.get(b).getTipoActividad().getNombre());
								linea.setAlumno(pslist.get(b).getAlumno().getNombre());
								linea.setIngreso(pslist.get(b).getAlumno().getCicloLectivo());
								linealist.add(linea);
							}
						}
					}
				}
			}
			
		//Con Org y Por Periodo
			if ((desde!=null) && (hasta!=null)){
				planesPeriodo = ptdao.findByPeriodo(desde, hasta);
				for (int a=0; a<planesPeriodo.size(); a++){
					for (int b=0; b<pslist.size(); b++){
						if (planesPeriodo.get(a).getPs().getId()==pslist.get(b).getId()){
							if (
									((pslist.get(b).getCuatrimestre()==cuatrimestre) || (cuatrimestre==0))
									&&
									((pslist.get(b).getCicloLectivo()==cicloLectivo) || (cicloLectivo==0))
									&&
									(pslist.get(b).getOrganizacion().getNombre().equals(org))
								){
								linea = new LineaDeReporte();
								linea.setFechaDePresentacion(planesPeriodo.get(a).getFechaDePresentacion());
								linea.setTitulo(pslist.get(b).getTitulo());
								linea.setEstado(pslist.get(b).getEstado().getNombre());
								linea.setArea(pslist.get(b).getOrganizacion().getNombre());
								linea.setTipoActividad(pslist.get(b).getTipoActividad().getNombre());
								linea.setAlumno(pslist.get(b).getAlumno().getNombre());
								linea.setIngreso(pslist.get(b).getAlumno().getCicloLectivo());
								linealist.add(linea);
							}
						}
					}
				}
			} else {
				//Con Org, Por CicloLectivo
				pslistCL = psdao.findByCicloLectivo(cicloLectivo);
				for (int i=0; i<pslistCL.size(); i++){			
					for (int j=0; j<planes.size(); j++){
						if (pslistCL.get(i).getId()==planes.get(j).getPs().getId()){
							if ((pslistCL.get(i).getCuatrimestre()==cuatrimestre) || (cuatrimestre==0)){
								if (pslistCL.get(i).getOrganizacion().getNombre().equals(org)){
									linea = new LineaDeReporte();
									linea.setFechaDePresentacion(planes.get(j).getFechaDePresentacion());
									linea.setTitulo(pslistCL.get(i).getTitulo());
									linea.setEstado(pslistCL.get(i).getEstado().getNombre());
									linea.setArea(pslistCL.get(i).getOrganizacion().getNombre());
									linea.setTipoActividad(pslistCL.get(i).getTipoActividad().getNombre());
									linea.setAlumno(pslistCL.get(i).getAlumno().getNombre());
									linea.setIngreso(pslistCL.get(i).getAlumno().getCicloLectivo());
									linealist.add(linea);
								}
							}
						}
					}
				}
				if (cicloLectivo==0){
					//Con Org, Por Cuatrimestre
					pslistCuatri = psdao.findByCuatrimestre(cuatrimestre);
					for (int x=0; x<pslistCuatri.size(); x++){			
						for (int y=0; y<planes.size(); y++){
							if (pslistCuatri.get(x).getId()==planes.get(y).getPs().getId()){
								if (pslistCuatri.get(x).getOrganizacion().getNombre().equals(org)){
									linea = new LineaDeReporte();
									linea.setFechaDePresentacion(planes.get(y).getFechaDePresentacion());
									linea.setTitulo(pslistCuatri.get(x).getTitulo());
									linea.setEstado(pslistCuatri.get(x).getEstado().getNombre());
									linea.setArea(pslistCuatri.get(x).getOrganizacion().getNombre());
									linea.setTipoActividad(pslistCuatri.get(x).getTipoActividad().getNombre());
									linea.setAlumno(pslistCuatri.get(x).getAlumno().getNombre());
									linea.setIngreso(pslistCuatri.get(x).getAlumno().getCicloLectivo());
									linealist.add(linea);
								}
							}
						}
					}
				}
			}
		}

		
		//Eliminar Repetidos
		boolean repe = false;
		for (int e=0; e<linealist.size(); e++){
			if (resultlist.isEmpty()){
				resultlist.add(linealist.get(e));
			}
			for (int f=0; f<resultlist.size(); f++){
				if (linealist.get(e).getFechaDePresentacion()==resultlist.get(f).getFechaDePresentacion()){
					repe = true;
				}				
			}
			if (repe==false){
				resultlist.add(linealist.get(e));
			}
			repe = false;
		}
		
				
	//Totales
		//Planes Presentados
		cPP = resultlist.size();
		
		if (cPP!=0){
				
			if (!(org.isEmpty())){
				totOrg = true;
				totTodos = false;
				
				//Porcentaje Planes Presentados
				double contPPre = 0;
				for (int q=0; q<resultlist.size(); q++){
					if (resultlist.get(q).getEstado().equals("Plan presentado")){
						contPPre++;
					}
				}
				cPPre = ((contPPre/cPP)*100);
				
				//Porcentaje Planes Aprobados
				double contPA = 0;
				for (int p=0; p<resultlist.size(); p++){
					if (
							(resultlist.get(p).getEstado().equals("Plan aprobado"))
							||
							(resultlist.get(p).getEstado().equals("Informe presentado"))
							||
							(resultlist.get(p).getEstado().equals("Informe observado"))
							||
							(resultlist.get(p).getEstado().equals("Informe aprobado"))
							||
							(resultlist.get(p).getEstado().equals("Informe vencido"))
							||
							(resultlist.get(p).getEstado().equals("PS aprobada"))
						){
						contPA++;
					}
				}
				cPA = ((contPA/cPP)*100);			

				//Porcentaje Planes Observados
				double contPO = 0;
				for (int q=0; q<resultlist.size(); q++){
					if (resultlist.get(q).getEstado().equals("Plan observado")){
						contPO++;
					}
				}
				cPO = ((contPO/cPP)*100);
				
				//Porcentaje Planes Rechazados
				double contPR = 0;
				for (int q=0; q<resultlist.size(); q++){
					if (resultlist.get(q).getEstado().equals("Plan rechazado")){
						contPR++;
					}
				}
				cPR = ((contPR/cPP)*100);
				
				//Porcentaje Planes Vencidos
				double contPV = 0;
				for (int r=0; r<resultlist.size(); r++){
					if (resultlist.get(r).getEstado().equals("Plan vencido")){
						contPV++;
					}
				}
				cPV = ((contPV/cPP)*100);
				
			} else {
				totOrg = false;
				totTodos = true;
				
				
				for (int g=0; g<orglist.size(); g++){
					double contador = 0;
					for (int h=0; h<resultlist.size(); h++){
						if (resultlist.get(h).getArea().equals(orglist.get(g).getNombre())){
							contador++;							
						}
					}
					linPorc = new LineaPorcentaje();
					linPorc.setArea(orglist.get(g).getNombre());
					linPorc.setContador(contador);
					linPorc.setPorcentaje(((contador*100)/cPP));
					porclist.add(linPorc);
				}
				
				for (int n=0; n<porclist.size(); n++){
					if (porclist.get(n).getContador()!=0){
						auxporclist.add(porclist.get(n));
					}
				}
								
			}
						
		}
		
	//Redireccion
		if (resultlist.size()==0){
			return "estPTporOrgNohay";
		} else {
			return "estPTporOrgListado";
		}

	}

	
	public String volver(){
		return "estPTporOrg";
	}
	
	
	
	public List<PS> getPslist() {
		return pslist;
	}

	public void setPslist(List<PS> pslist) {
		this.pslist = pslist;
	}

	public List<PS> getPslistCL() {
		return pslistCL;
	}

	public void setPslistCL(List<PS> pslistCL) {
		this.pslistCL = pslistCL;
	}

	public List<PS> getPslistCuatri() {
		return pslistCuatri;
	}

	public void setPslistCuatri(List<PS> pslistCuatri) {
		this.pslistCuatri = pslistCuatri;
	}

	public List<PlanDeTrabajo> getPlanes() {
		return planes;
	}

	public void setPlanes(List<PlanDeTrabajo> planes) {
		this.planes = planes;
	}

	public List<PlanDeTrabajo> getPlanesPeriodo() {
		return planesPeriodo;
	}

	public void setPlanesPeriodo(List<PlanDeTrabajo> planesPeriodo) {
		this.planesPeriodo = planesPeriodo;
	}

	public List<LineaDeReporte> getLinealist() {
		return linealist;
	}

	public void setLinealist(List<LineaDeReporte> linealist) {
		this.linealist = linealist;
	}

	public List<LineaDeReporte> getResultlist() {
		return resultlist;
	}

	public void setResultlist(List<LineaDeReporte> resultlist) {
		this.resultlist = resultlist;
	}

	public List<LineaPorcentaje> getAuxporclist() {
		return auxporclist;
	}

	public void setAuxporclist(List<LineaPorcentaje> auxporclist) {
		this.auxporclist = auxporclist;
	}

	public List<LineaPorcentaje> getPorclist() {
		return porclist;
	}

	public void setPorclist(List<LineaPorcentaje> porclist) {
		this.porclist = porclist;
	}

	public List<Organizacion> getOrglist() {
		return orglist;
	}

	public void setOrglist(List<Organizacion> orglist) {
		this.orglist = orglist;
	}

	public String getOrg() {
		return org;
	}

	public void setOrg(String org) {
		this.org = org;
	}

	public Map<String, String> getOrgs() {
		return orgs;
	}

	public void setOrgs(Map<String, String> orgs) {
		this.orgs = orgs;
	}

	public int getCicloLectivo() {
		return cicloLectivo;
	}

	public void setCicloLectivo(int cicloLectivo) {
		this.cicloLectivo = cicloLectivo;
	}

	public int getCuatrimestre() {
		return cuatrimestre;
	}

	public void setCuatrimestre(int cuatrimestre) {
		this.cuatrimestre = cuatrimestre;
	}

	public Date getDesde() {
		return desde;
	}

	public void setDesde(Date desde) {
		this.desde = desde;
	}

	public Date getHasta() {
		return hasta;
	}

	public void setHasta(Date hasta) {
		this.hasta = hasta;
	}
	
	public LineaDeReporte getLinea() {
		return linea;
	}

	public void setLinea(LineaDeReporte linea) {
		this.linea = linea;
	}

	public LineaPorcentaje getLinPorc() {
		return linPorc;
	}

	public void setLinPorc(LineaPorcentaje linPorc) {
		this.linPorc = linPorc;
	}

	public boolean isTotOrg() {
		return totOrg;
	}

	public void setTotOrg(boolean totOrg) {
		this.totOrg = totOrg;
	}

	public boolean isTotTodos() {
		return totTodos;
	}

	public void setTotTodos(boolean totTodos) {
		this.totTodos = totTodos;
	}

	public double getcPP() {
		return cPP;
	}

	public void setcPP(double cPP) {
		this.cPP = cPP;
	}

	public double getcPPre() {
		return cPPre;
	}

	public void setcPPre(double cPPre) {
		this.cPPre = cPPre;
	}

	public double getcPA() {
		return cPA;
	}

	public void setcPA(double cPA) {
		this.cPA = cPA;
	}

	public double getcPO() {
		return cPO;
	}

	public void setcPO(double cPO) {
		this.cPO = cPO;
	}

	public double getcPR() {
		return cPR;
	}

	public void setcPR(double cPR) {
		this.cPR = cPR;
	}

	public double getcPV() {
		return cPV;
	}

	public void setcPV(double cPV) {
		this.cPV = cPV;
	}
	
}
