package com.smartps.dao;

import java.util.List;

import org.hibernate.Session;

import com.smartps.model.TipoActividad;
import com.smartps.util.HibernateUtil;

public class TipoActividadDao {	
	private static TipoActividadDao instancia;
	private Session session = HibernateUtil.getSessionFactory().getCurrentSession();
	
	protected  TipoActividadDao(){
		
	}
	
	public static TipoActividadDao getInstance(){
		if (instancia == null){
			instancia= new TipoActividadDao();
		}
		return instancia;
	}
	
	public List<TipoActividad> getAll(){
		return (session.createQuery("from TipoActividad").list());
		
		
		
}
	
	public void save(TipoActividad tActividad) {
		session= HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		session.save(tActividad);
		session.getTransaction().commit();
	}

	public void update(TipoActividad objeto) {
		// TODO Auto-generated method stub
		
	}

	public void delete(TipoActividad objeto) {
		// TODO Auto-generated method stub
		
	}
	
}
