package chasqui.dao.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.joda.time.DateTime;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.vividsolutions.jts.geom.Point;

import chasqui.dao.ZonaDAO;
import chasqui.model.Zona;

public class ZonaDAOHbm extends HibernateDaoSupport implements ZonaDAO{

	
	
	public void guardar(Zona z) {
		this.getHibernateTemplate().saveOrUpdate(z);
		
	}

	public void eliminar(Zona z) {
	this.getHibernateTemplate().delete(z);
		
	}

	public List<Zona> buscarZonasBy(final Integer idVendedor) {
		List<Zona>zs = this.getHibernateTemplate().execute(new HibernateCallback<List<Zona>>() {

			@SuppressWarnings("unchecked")
			public List<Zona> doInHibernate(Session session) throws HibernateException, SQLException {
				Criteria c = session.createCriteria(Zona.class);
				c.add(Restrictions.eq("idVendedor", idVendedor));
				c.addOrder(Order.asc("nombre"));
				return (List<Zona>) c.list();
			}
		});
		if(zs == null){
			return new ArrayList<Zona>();
		}
		return zs;
	}
	
	public Zona ObtenerZonaPorID(final Integer idVendedor, final Integer idZona) {
		Zona zona = this.getHibernateTemplate().execute(new HibernateCallback<Zona>() {

			public Zona doInHibernate(Session session) throws HibernateException, SQLException {
				Criteria c = session.createCriteria(Zona.class);
				c.add(Restrictions.eq("idVendedor", idVendedor));
				c.add(Restrictions.eq("id", idZona));
				return (Zona) c.list().get(0);
			}
		});
		return zona;
	}

	@Override
	public List<Zona> obtenerZonas(final Integer idVendedor) {
		List<Zona> zona = this.getHibernateTemplate().execute(new HibernateCallback<List<Zona>>() {

			public List<Zona> doInHibernate(Session session) throws HibernateException, SQLException {
				Criteria c = session.createCriteria(Zona.class);
				c.add(Restrictions.eq("idVendedor", idVendedor));
				c.addOrder(Order.asc("nombre"));
				return (List<Zona>) c.list();
			}
		});
		return zona;
	}

	@Override
	public Zona buscarZonaProxima(final Integer idVendedor) {
		Zona zona = this.getHibernateTemplate().execute(new HibernateCallback<Zona>() {

			public Zona doInHibernate(Session session) throws HibernateException, SQLException {
				DateTime now = new DateTime();
				Criteria c = session.createCriteria(Zona.class);
				c.add(Restrictions.eq("idVendedor", idVendedor));
				c.add(Restrictions.ge("fechaCierrePedidos", now));
				c.addOrder(Order.asc("fechaCierrePedidos"));
				if(c.list().size() > 0){
					return (Zona) c.list().get(0);
				}else{
					return null;
				}
			}
		});
		return zona;
	}

	@Override
	public Zona obtenerZonaPorId(final Integer zonaID) {
		Zona zona = this.getHibernateTemplate().execute(new HibernateCallback<Zona>() {

			public Zona doInHibernate(Session session) throws HibernateException, SQLException {
				Criteria c = session.createCriteria(Zona.class);
				c.add(Restrictions.eq("id", zonaID));
				return (Zona) c.uniqueResult();
			}
		});
		return zona;
	}
	
	@Override
	public Zona obtenerZonaPorNombre(final String nombre, final Integer idVendedor) {
		Zona zona = this.getHibernateTemplate().execute(new HibernateCallback<Zona>() {

			public Zona doInHibernate(Session session) throws HibernateException, SQLException {
				Criteria c = session.createCriteria(Zona.class);
				c.add(Restrictions.eq("nombre", nombre));
				c.add(Restrictions.eq("idVendedor", idVendedor));
				return (Zona) c.uniqueResult();
			}
		});
		return zona;
	}
	
	@Override
	public Zona obtenerZonaDePertenenciaDeDireccion(final Point punto, final Integer idVendedor){
		Zona zona = this.getHibernateTemplate().execute(new HibernateCallback<Zona>() {

			public Zona doInHibernate(Session session) throws HibernateException, SQLException {
				Query q = session.createQuery("FROM Zona WHERE ID_VENDEDOR = :idVendedor and true = ST_CONTAINS(GEO_AREA, Point(:pointX, :pointY))");
				q.setInteger("idVendedor", idVendedor);
				q.setDouble("pointX", punto.getX());
				q.setDouble("pointY", punto.getY());

				return (Zona) q.uniqueResult();
			}
		});
		return zona;
		
	}
	
	
	
}
