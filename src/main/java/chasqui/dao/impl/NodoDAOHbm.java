package chasqui.dao.impl;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import org.apache.cxf.common.util.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.joda.time.DateTime;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import chasqui.dao.NodoDAO;
import chasqui.dtos.queries.NodoQueryDTO;
import chasqui.model.Nodo;
import chasqui.model_lite.NodoLite;
import chasqui.view.composer.Constantes;

public class NodoDAOHbm extends HibernateDaoSupport implements NodoDAO {

	
	
//	public void altaSolicitudNodo(String alias) {
//		// TODO Auto-generated method stub
//
//	}
	@Override
	public void aprobarNodo(Integer id) {
		// TODO Auto-generated method stub
		Nodo nodo = this.obtenerNodoPorId(id);
		this.getHibernateTemplate().saveOrUpdate(nodo);
		this.getHibernateTemplate().flush();
	}
	@Override
	public void guardarNodos(List<Nodo> nodos) {
		for(Nodo nodo: nodos) {
			this.getHibernateTemplate().saveOrUpdate(nodo);
		}
		this.getHibernateTemplate().flush();		
	}
	@Override
	public void guardarNodo(Nodo nodo) {
		this.getHibernateTemplate().saveOrUpdate(nodo);
		this.getHibernateTemplate().flush();
	}

	public List<Nodo> obtenerNodosDelVendedor(final Integer idVendedor) {
		return this.getHibernateTemplate().execute(new HibernateCallback<List<Nodo>>() {

			@Override
			public List<Nodo> doInHibernate(Session session) throws HibernateException, SQLException {
				Criteria criteria = session.createCriteria(Nodo.class);
				criteria.add(Restrictions.eq("vendedor.id", idVendedor))
				.add(Restrictions.eq("esNodo", true))
				.addOrder(Order.desc("id"));
				return (List<Nodo>) criteria.list();
			}

		});

	}

	public Nodo obtenerNodoPorId(final Integer idNodo) {
		return this.getHibernateTemplate().execute(new HibernateCallback<Nodo>() {

			@Override
			public Nodo doInHibernate(Session session) throws HibernateException, SQLException {
				Criteria criteria = session.createCriteria(Nodo.class);
				criteria.add(Restrictions.eq("id", idNodo))
				.add(Restrictions.eq("esNodo", true)); 
				return (Nodo) criteria.uniqueResult();
			}

		});
	}

	public void eliminarNodo(Integer idNodo) {
		this.getHibernateTemplate().delete(this.obtenerNodoPorId(idNodo));
		this.getHibernateTemplate().flush();

	}


	public Nodo obtenerNodoPorAlias(final String alias) {
		return this.getHibernateTemplate().execute(new HibernateCallback<Nodo>() {

			@Override
			public Nodo doInHibernate(Session session) throws HibernateException, SQLException {
				Criteria criteria = session.createCriteria(Nodo.class);
				criteria.add(Restrictions.eq("alias", alias))
				.add(Restrictions.eq("esNodo", true)); 
				return (Nodo) criteria.uniqueResult();
			}

		});
	}
	
	@Override
	public List<Nodo> obtenerNodosDelCliente(final Integer idVendedor, final String email) {
		return this.getHibernateTemplate().execute(new HibernateCallback<List<Nodo>>() {

			@Override
			public List<Nodo> doInHibernate(Session session) throws HibernateException, SQLException {
				Criteria criteria = session.createCriteria(Nodo.class);
				criteria.add(Restrictions.eq("vendedor.id", idVendedor))
				.add(Restrictions.eq("esNodo", true))
	    		.createCriteria("cache").add(Restrictions.eq("email", email))
	    		.add(Restrictions.eq("estadoInvitacion",Constantes.ESTADO_NOTIFICACION_LEIDA_ACEPTADA));
				
				return (List<Nodo>) criteria.list();
			}

		});

	}

	public List<Nodo> obtenerNodosAbiertosDelVendedor(final Integer idVendedor) {
		return this.getHibernateTemplate().execute(new HibernateCallback<List<Nodo>>() {

			@Override
			public List<Nodo> doInHibernate(Session session) throws HibernateException, SQLException {
				Criteria criteria = session.createCriteria(Nodo.class);
				criteria.add(Restrictions.eq("vendedor.id", idVendedor))
				.add(Restrictions.eq("esNodo", true))
				.add(Restrictions.eq("tipo",Constantes.NODO_ABIERTO))
				.createCriteria("pedidoActual")
				.add(Restrictions.ne("estado", Constantes.ESTADO_PEDIDO_CANCELADO));
				
				return (List<Nodo>) criteria.list();
			}

		});
	}
	
	/** @deprecated use obtenerNodos */
	public List<Nodo> obtenerNodosDelVendedorCon(final Integer idVendedor, final Date d, final Date h, final String estadoNodo,
			final String nombreNodo, final String emailcoordinador, final String barrio, final String tipo, final Integer idZona) {
		return this.getHibernateTemplate().execute(new HibernateCallback<List<Nodo>>() {

			@SuppressWarnings("unchecked")
			@Override
			public List<Nodo> doInHibernate(Session session) throws HibernateException, SQLException {
				Criteria c = session.createCriteria(Nodo.class);
				c.add(Restrictions.eq("vendedor.id", idVendedor))
				.add(Restrictions.eq("esNodo", true));
				
				if(!StringUtils.isEmpty(tipo)) {
					c.add(Restrictions.eq("tipo", tipo));
				}
				if (!StringUtils.isEmpty(nombreNodo)) {
					c.add(Restrictions.like("alias", "%"+nombreNodo+"%"));
				}
				if (d != null && h != null) {
					DateTime desde = new DateTime(d.getTime());
					DateTime hasta = new DateTime(h.getTime());
					c.add(Restrictions.between("fechaCreacion", desde.withHourOfDay(0), hasta.plusDays(1).withHourOfDay(0)));
				}else{
					if(d!=null){
						DateTime desde = new DateTime(d.getTime());
						c.add(Restrictions.ge("fechaCreacion", desde.withHourOfDay(0)));
					}else{
						if(h!=null){
							DateTime hasta = new DateTime(h.getTime());
							c.add(Restrictions.le("fechaCreacion", hasta.plusDays(1).withHourOfDay(0)));
						}
					}
				}
				if(!StringUtils.isEmpty(estadoNodo)) {
					boolean activo = estadoNodo.equals(Constantes.NODO_ACTIVO); 
					c.add(Restrictions.eq("activo",activo));
				}
				if(!StringUtils.isEmpty(emailcoordinador)) {
					c.add(Restrictions.like("emailAdministradorNodo", "%"+emailcoordinador+"%"));
				}
				if(!StringUtils.isEmpty(barrio)) {
					c.add(Restrictions.like("barrio", "%"+barrio+"%"));
				}
				if(idZona != null) {
					c.add(Restrictions.eq("zona.id", idZona));
				}
				c.addOrder(Order.desc("id"));
				return (List<Nodo>) c.list();
			}

		});
	}

	public List<Nodo> obtenerNodos(final NodoQueryDTO query) {

		final String orderBy = query.getOrderBy();
		final Boolean isOrderDesc = "desc".equals(query.getOrderDirection());
		final Integer skip = query.getSkip();
		final Integer limit = query.getLimit();
		
		return this.getHibernateTemplate().execute(new HibernateCallback<List<Nodo>>() {

			@SuppressWarnings("unchecked")
			@Override
			public List<Nodo> doInHibernate(Session session) throws HibernateException, SQLException {
				Criteria c = makeCriteria(session, query);
				c.createAlias("nodo.administrador", "administrador");
				c.createAlias("nodo.direccionDelNodo", "direccionDelNodo");
				c.createAlias("nodo.zona", "zona");
				
				if(!StringUtils.isEmpty(orderBy)) {
					if(isOrderDesc) {
						c.addOrder(Order.desc(orderBy));
					} else {
						c.addOrder(Order.asc(orderBy));
					}
				}

				c.setFirstResult(skip);
				c.setMaxResults(limit);
				
				return (List<Nodo>) c.list();
			}
		});
	}

	public List<NodoLite> obtenerNodosLite(final NodoQueryDTO query) {
		return this.getHibernateTemplate().execute(new HibernateCallback<List<NodoLite>>() {

			@SuppressWarnings("unchecked")
			@Override
			public List<NodoLite> doInHibernate(Session session) throws HibernateException, SQLException {
				Criteria c = session.createCriteria(NodoLite.class, "nodo");
				
				return c.list();
			}
		});
	}

	public Long countNodos(final NodoQueryDTO query) {
		return this.getHibernateTemplate().execute(new HibernateCallback<Long>() {

			@Override
			public Long doInHibernate(Session session) throws HibernateException, SQLException {
				Criteria c = makeCriteria(session, query);
				c.setProjection(Projections.rowCount());
				return (Long) c.uniqueResult();
			}
		});
	}

	private Criteria makeCriteria(Session session, NodoQueryDTO query) {
		final Integer idVendedor = query.getIdVendedor();
		final Integer idNodo = query.getIdNodo();
		final String tipo = query.getTipo();
		final String nombreNodo = query.getNombre();
		final Date d = query.getDesde();
		final Date h = query.getHasta();
		final String estadoNodo = query.getEstado();
		final String emailcoordinador = query.getEmailCoordinador();
		final String barrio = query.getBarrio();
		final Integer idZona = query.getIdZona();

		Criteria c = session.createCriteria(Nodo.class, "nodo");
		c.add(Restrictions.eq("vendedor.id", idVendedor))
		.add(Restrictions.eq("esNodo", true));

		if(idNodo != null && idNodo > 0){
			c.add(Restrictions.like("nodo.id", idNodo));
			return c;
		}
		
		if(!StringUtils.isEmpty(tipo)) {
			c.add(Restrictions.eq("tipo", tipo));
		}
		if (!StringUtils.isEmpty(nombreNodo)) {
			c.add(Restrictions.like("alias", "%"+nombreNodo+"%"));
		}

		if (d != null && h != null) {
			DateTime desde = new DateTime(d.getTime());
			DateTime hasta = new DateTime(h.getTime());
			c.add(Restrictions.between("fechaCreacion", desde.withHourOfDay(0), hasta.plusDays(1).withHourOfDay(0)));
		}else{
			if(d!=null){
				DateTime desde = new DateTime(d.getTime());
				c.add(Restrictions.ge("fechaCreacion", desde.withHourOfDay(0)));
			}else{
				if(h!=null){
					DateTime hasta = new DateTime(h.getTime());
					c.add(Restrictions.le("fechaCreacion", hasta.plusDays(1).withHourOfDay(0)));
				}
			}
		}
		if(!StringUtils.isEmpty(estadoNodo)) {
			boolean activo = estadoNodo.equals(Constantes.NODO_ACTIVO); 
			c.add(Restrictions.eq("activo",activo));
		}
		if(!StringUtils.isEmpty(emailcoordinador)) {
			c.add(Restrictions.like("emailAdministradorNodo", "%"+emailcoordinador+"%"));
		}
		if(!StringUtils.isEmpty(barrio)) {
			c.add(Restrictions.like("barrio", "%"+barrio+"%"));
		}
		if(idZona != null) {
			c.add(Restrictions.eq("zona.id", idZona));
		}

		return c;
	}


}