package chasqui.dao.impl;

import java.sql.SQLException;
import java.util.List;
import java.util.Random;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.joda.time.DateTime;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import chasqui.dao.ProductoDAO;
import chasqui.exceptions.ProductoInexistenteException;
import chasqui.model.Caracteristica;
import chasqui.model.Imagen;
import chasqui.model.Producto;
import chasqui.model.Variante;

@SuppressWarnings("unchecked")
public class ProductoDAOHbm extends HibernateDaoSupport implements ProductoDAO{
	
	@Override
	public List<Variante> obtenerVariantesPorMultiplesFiltros(final Integer idVendedor, final Integer idCategoria, final List<Integer> idsSellosProducto, final Integer idProductor, final List<Integer> idsSellosProductor,final String query, final Integer pagina,
			final Integer cantidadDeItems, final Integer numeroDeOrden) {
		
		final Integer inicio = calcularInicio(pagina,cantidadDeItems);
		final Integer fin = calcularFin(pagina,cantidadDeItems);
		return this.getHibernateTemplate().execute(new HibernateCallback<List<Variante>>() {

			@Override
			public List<Variante> doInHibernate(Session session) throws HibernateException, SQLException {
				Criteria c = session.createCriteria(Variante.class, "variante");
				c.createAlias("variante.producto", "producto");
				c.createAlias("producto.fabricante", "fabricante");
				c.add(Restrictions.eq("fabricante.idVendedor",idVendedor));
				c.add(Restrictions.eq("producto.ocultado", false));
				if(query!=null){
					c.add(Restrictions.like("producto.nombre", "%"+query+"%"));
				}
				if(idCategoria != null){
					 c.createAlias("producto.categoria", "categoria")
					 .add(Restrictions.eq("categoria.id", idCategoria));
				}
				if(!idsSellosProducto.isEmpty() || !idsSellosProductor.isEmpty()) {
					Disjunction disjuntion = Restrictions.disjunction();
					if(!idsSellosProducto.isEmpty()) {
						c.createAlias("producto.caracteristicas","caracteristicasProducto");
						disjuntion.add(Restrictions.in("caracteristicasProducto.id", idsSellosProducto));
					}
					if(!idsSellosProductor.isEmpty()) {
						c.createAlias("fabricante.caracteristicas", "caracteristicasProductor");
						disjuntion.add(Restrictions.in("caracteristicasProductor.id", idsSellosProductor));
					}
					c.add(disjuntion);
				}
				 if(idProductor !=null){
				  c.add(Restrictions.eq("fabricante.id", idProductor));
				 }
				 c.add(Restrictions.sqlRestriction("( STOCK - RESERVADOS) > 0"))
				 //.addOrder(Order.asc("id"))
				 .addOrder(Order.desc("variante.destacado"));
				 definirOrdenRandom(c, numeroDeOrden);
				 c.setFirstResult(inicio)
				 .setMaxResults(cantidadDeItems);
				return (List<Variante>)c.list();
			}
		});
	}
	
	private void definirOrdenRandom(Criteria c, Integer numeroDeOrden) {
		if(numeroDeOrden == null) {
			numeroDeOrden = 0;
		}
		switch(numeroDeOrden%11) {
		  case 0:
			  c.addOrder(Order.asc("producto.nombre"));
		    break;
		  case 1:
			  c.addOrder(Order.asc("producto.categoria"));
		    break;
		  case 2:
			  c.addOrder(Order.asc("producto.fabricante"));
			break;
		  case 3:
			  c.addOrder(Order.desc("variante.stock"));
			  break;
		  case 4:
			  c.addOrder(Order.desc("producto.nombre"));
		    break;
		  case 5:
			  c.addOrder(Order.desc("producto.categoria"));
		    break;
		  case 6:
			  c.addOrder(Order.desc("producto.fabricante"));
			break;
		  case 7:
			  c.addOrder(Order.desc("variante.codigo"));
		  case 8:
			  c.addOrder(Order.asc("variante.codigo"));
		  case 9:
			  c.addOrder(Order.asc("variante.precio"));
		  case 10:
			  c.addOrder(Order.desc("variante.precio"));
		  default:
			  c.addOrder(Order.asc("id"));
		}
	}

	@Override
	public List<Variante> obtenerTodasLasVariantes(final Integer idVendedor) {

		return this.getHibernateTemplate().execute(new HibernateCallback<List<Variante>>() {

			@Override
			public List<Variante> doInHibernate(Session session) throws HibernateException, SQLException {
				Criteria c = session.createCriteria(Variante.class, "variante");
				c.createAlias("variante.producto", "producto");
				c.createAlias("producto.fabricante", "fabricante");
				c.add(Restrictions.eq("fabricante.idVendedor",idVendedor));
				return (List<Variante>)c.list();
			}
		});
	}

	@Override
	public Long obtenerTotalVariantesPorMultiplesFiltros(final Integer idVendedor, final Integer idCategoria, final List<Integer> idsSellosProducto, final Integer idProductor, final List<Integer> idsSellosProductor,final String query) {
		
		return this.getHibernateTemplate().execute(new HibernateCallback<Long>() {

			@Override
			public Long doInHibernate(Session session) throws HibernateException, SQLException {
				Criteria c = session.createCriteria(Variante.class, "variante");
				c.createAlias("variante.producto", "producto");
				c.createAlias("producto.fabricante", "fabricante");
				c.add(Restrictions.eq("fabricante.idVendedor",idVendedor));
				c.add(Restrictions.eq("producto.ocultado", false));
				if(query!=null){
					c.add(Restrictions.like("producto.nombre", "%"+query+"%"));
				}
				if(idCategoria != null){
					 c.createAlias("producto.categoria", "categoria")
					 .add(Restrictions.eq("categoria.id", idCategoria));
				}
				if(!idsSellosProducto.isEmpty()) {
					c.createAlias("producto.caracteristicas","caracteristicasProducto");
					c.add(Restrictions.in("caracteristicasProducto.id", idsSellosProducto));
				}
				if(!idsSellosProductor.isEmpty()) {
					c.createAlias("fabricante.caracteristicas", "caracteristicasProductor");
					c.add(Restrictions.in("caracteristicasProductor.id", idsSellosProductor));
				}
				 if(idProductor !=null){
				  c.add(Restrictions.eq("fabricante.id", idProductor));
				 }
				 c.add(Restrictions.sqlRestriction("( STOCK - RESERVADOS) > 0"))
				 .setProjection(Projections.rowCount());
				return (Long) c.uniqueResult();
			}
		});
	}
	
	@Override
	@Deprecated
	public Long totalVariantesBajoMultiplesFiltros(final Integer idCategoria, final Integer idMedalla, final Integer idProductor) {
		return this.getHibernateTemplate().execute(new HibernateCallback<Long>() {

			@Override
			public Long doInHibernate(Session session) throws HibernateException, SQLException {
				Criteria c = session.createCriteria(Variante.class);
				c.createAlias("producto", "p");
				 if(idCategoria != null){
					 c.createAlias("p.categoria", "categoria")
					 .add(Restrictions.eq("categoria.id", idCategoria));
				 }
				 if(idMedalla != null){
					 c.createAlias("p.caracteristicas","caracteristicas")
					 .add(Restrictions.eq("caracteristicas.id", idMedalla));
				 }
				 if(idProductor !=null){
				  c.createAlias("p.fabricante", "productor")
				  .add(Restrictions.eq("productor.id", idProductor));
				 }
				 c.add(Restrictions.eq("p.ocultado", false));
				 c.setProjection(Projections.rowCount());				
				return  (Long)c.uniqueResult();
			}
		});
	}
	@Deprecated
	@Override
	public List<Variante> obtenerVariantesPorCategoria(final Integer idCategoria, final Integer pagina,
			final Integer cantidadDeItems) {
		
		final Integer inicio = calcularInicio(pagina,cantidadDeItems);
		final Integer fin = calcularFin(pagina,cantidadDeItems);
		return this.getHibernateTemplate().execute(new HibernateCallback<List<Variante>>() {

			@Override
			public List<Variante> doInHibernate(Session session) throws HibernateException, SQLException {
				Criteria c = session.createCriteria(Variante.class);
				c.createAlias("producto", "p")
				 .createAlias("p.categoria", "c")
				 .add(Restrictions.eq("c.id", idCategoria))
				 .add(Restrictions.sqlRestriction("( STOCK - RESERVADOS) > 0"))
				 .add(Restrictions.eq("p.ocultado", false))
				 .addOrder(Order.asc("id"))
				 .setFirstResult(inicio )
				 .setMaxResults(cantidadDeItems);
				return (List<Variante>)c.list();
			}
		});
	}
	@Deprecated
	@Override
	public List<Variante> obtenerVariantesPorProductor(final Integer idProductor, final Integer pagina, final Integer cantItems) {
		final Integer inicio = calcularInicio(pagina,cantItems);
		final Integer fin = calcularFin(pagina,cantItems);
		return this.getHibernateTemplate().executeFind(new HibernateCallback<List<Variante>>() {

			@Override
			public List<Variante> doInHibernate(Session session) throws HibernateException, SQLException {
				Criteria c = session.createCriteria(Variante.class);
				c.createAlias("producto", "p")
				 .createAlias("p.fabricante", "f")
				 .add(Restrictions.eq("f.id", idProductor))
				 .add(Restrictions.sqlRestriction("( STOCK - RESERVADOS) > 0"))
				 .add(Restrictions.eq("p.ocultado", false))
				 .setFirstResult(inicio)
				 .setMaxResults(cantItems)
				 .addOrder(Order.asc("id"));
				return (List<Variante>)c.list();
			}
		});
	}
	
	@Deprecated
	@Override
	public List<Variante> obtenerVariantesPorMedalla(final Integer idMedalla, final Integer pagina, final Integer cantItems,final Integer idVendedor) {
		final Integer inicio = calcularInicio(pagina,cantItems);
		final Integer fin = calcularFin(pagina,cantItems);
		return this.getHibernateTemplate().executeFind(new HibernateCallback<List<Variante>>() {

			@Override
			public List<Variante> doInHibernate(Session session) throws HibernateException, SQLException {
				Criteria c = session.createCriteria(Variante.class);
				c.createAlias("producto", "p")
				 .createAlias("p.caracteristicas", "m")
				 .createAlias("p.fabricante", "f")
				 .add(Restrictions.eq("m.id", idMedalla))
				 .add(Restrictions.eq("f.idVendedor",idVendedor))
				 .add(Restrictions.sqlRestriction("( STOCK - RESERVADOS) > 0"))
				 .add(Restrictions.eq("p.ocultado", false))
				 .setFirstResult(inicio)
				 .setMaxResults(cantItems)
				 .addOrder(Order.asc("id"));
				return  (List<Variante>)c.list();
			}
		});
	}
	
	
	@Deprecated
	@Override
	public List<Variante> obtenerVariantesSinFiltro(Integer pagina, final Integer cantItems, final Integer idVendedor) {
		final Integer inicio = calcularInicio(pagina,cantItems);
		return this.getHibernateTemplate().executeFind(new HibernateCallback<List<Variante>>() {

			@Override
			public List<Variante> doInHibernate(Session session) throws HibernateException, SQLException {
				Criteria c = session.createCriteria(Variante.class);
				c.createAlias("producto", "p")
				 .createAlias("p.fabricante", "f")
				 .add(Restrictions.eq("f.idVendedor",idVendedor))
				 .add(Restrictions.sqlRestriction("( STOCK - RESERVADOS) > 0"))
				 .add(Restrictions.eq("p.ocultado", false))
				 .setFirstResult(inicio)
				 .setMaxResults(cantItems)
				 .addOrder(Order.asc("id"));
				return  (List<Variante>)c.list();
			}
		});
	}
	

	@Override
	public List<Imagen> obtenerImagenesDe(final Integer idProducto) {
		return this.getHibernateTemplate().execute(new HibernateCallback<List<Imagen>>() {

			@Override
			public List<Imagen> doInHibernate(Session session) throws HibernateException, SQLException {
				Criteria c = session.createCriteria(Variante.class);
				c.add(Restrictions.eq("id", idProducto));
				Variante v = (Variante) c.uniqueResult();
				if(v != null){
					return v.getImagenes();
				}
				throw new ProductoInexistenteException();
			}
		});
	}
	

	private Integer calcularFin(Integer pagina, Integer cantidadDeItems) {
		return calcularInicio(pagina,cantidadDeItems) + cantidadDeItems;
	}

	private Integer calcularInicio(Integer pagina, Integer cantidadDeItems) {		
		if(pagina == 1){
			return 0;
		}
		return (pagina - 1) * cantidadDeItems;
	}

	@Override
	public List<Variante> obtenerVariantesPorNombreODescripcion(final String param,Integer pagina,final Integer cantItems,final Integer idVendedor) {
		final Integer inicio = calcularInicio(pagina,cantItems);
		return this.getHibernateTemplate().executeFind(new HibernateCallback<List<Variante>>() {

			@Override
			public List<Variante> doInHibernate(Session session) throws HibernateException, SQLException {
				Criteria c = session.createCriteria(Variante.class);
				Disjunction or = Restrictions.disjunction();
				or.add(Restrictions.like("nombre","%" +param+ "%" ));
				or.add(Restrictions.like("p.nombre","%" +param+ "%"));
				or.add(Restrictions.like("descripcion","%" +param+ "%"));
				c.createAlias("producto", "p")
				 .createAlias("p.categoria", "c")
				 .createAlias("c.vendedor", "v")
				 .add(Restrictions.eq("p.ocultado", false))
				 .add(Restrictions.eq("v.id", idVendedor))
				 .add(Restrictions.sqlRestriction("( STOCK - RESERVADOS) > 0"))
				//OCULTADO .add(Restrictions.eq("p.ocultado", false))
				 .add(or)
				 .setFirstResult(inicio)
				 .setMaxResults(cantItems)
				 .addOrder(Order.asc("p.nombre"));
				return  (List<Variante>)c.list();
			}
		});
	}

	@Override
	public Variante obtenervariantePor(final Integer id) {
		return this.getHibernateTemplate().execute(new HibernateCallback<Variante>() {

			@Override
			public Variante doInHibernate(Session session) throws HibernateException, SQLException {
				Criteria criteria = session.createCriteria(Variante.class);
				criteria.add(Restrictions.eq("id", id));
				return (Variante) criteria.uniqueResult();
			}
		});
	}
	
	@Override
	public Variante obtenervariantePorCodigoProducto(final String codigoProducto, final Integer idVendedor) {
		return this.getHibernateTemplate().execute(new HibernateCallback<Variante>() {

			@Override
			public Variante doInHibernate(Session session) throws HibernateException, SQLException {
				Criteria criteria = session.createCriteria(Variante.class);
				criteria.createAlias("producto", "p")
				.createAlias("p.fabricante", "f")
				.add(Restrictions.eq("f.idVendedor",idVendedor))
				.add(Restrictions.like("codigo", codigoProducto, MatchMode.EXACT));
				return (Variante) criteria.uniqueResult();
			}
		});
	}

	@Override
	public void modificarVariante(Variante v) {
		this.getHibernateTemplate().saveOrUpdate(v);
		this.getHibernateTemplate().flush();		
	}

	@Override
	public Caracteristica obtenerCaracteristicaPor(final Integer idMedalla) {
		return this.getHibernateTemplate().execute(new HibernateCallback<Caracteristica>() {

			@Override
			public Caracteristica doInHibernate(Session session) throws HibernateException, SQLException {
				Criteria criteria = session.createCriteria(Caracteristica.class);
				criteria.add(Restrictions.eq("id", idMedalla));
				return (Caracteristica) criteria.uniqueResult();
			}
		});
		
	}

	@Override
	public Long totalVariantesPorCategoria(final Integer idCategoria) {
		return this.getHibernateTemplate().execute(new HibernateCallback<Long>() {

			@Override
			public Long doInHibernate(Session session) throws HibernateException, SQLException {
				Criteria c = session.createCriteria(Variante.class);
				c.createAlias("producto", "p")
				 .createAlias("p.categoria", "c")
				 .add(Restrictions.eq("p.ocultado", false))
				 .add(Restrictions.eq("c.id", idCategoria))
				 .setProjection(Projections.rowCount());				
				return  (Long)c.uniqueResult();
			}
		});
	}

	@Override
	public Long totalVariantesPorProductor(final Integer idProductor) {
		return this.getHibernateTemplate().execute(new HibernateCallback<Long>() {

			@Override
			public Long doInHibernate(Session session) throws HibernateException, SQLException {
				Criteria c = session.createCriteria(Variante.class);
				c.createAlias("producto", "p")
				 .createAlias("p.fabricante", "f")
				 .add(Restrictions.eq("f.id", idProductor))
				 .add(Restrictions.eq("p.ocultado", false))
				 .setProjection(Projections.rowCount());				
				return  (Long)c.uniqueResult();
			}
		});
	}

	@Override
	public Long totalVariantesPorMedalla(final Integer idMedalla) {
		return this.getHibernateTemplate().execute(new HibernateCallback<Long>() {

			@Override
			public Long doInHibernate(Session session) throws HibernateException, SQLException {
				Criteria c = session.createCriteria(Variante.class);
				c.createAlias("producto", "p")
				 .createAlias("p.caracteristicas", "m")
				 .add(Restrictions.eq("m.id", idMedalla))
				 .add(Restrictions.eq("p.ocultado", false))
				 .setProjection(Projections.rowCount());
				
				return  (Long)c.uniqueResult();
			}
		});
	}

	@Override
	public Long totalVariantesPorNombreODescripcion(final String param,final Integer idVendedor) {
		return this.getHibernateTemplate().execute(new HibernateCallback<Long>() {

			@Override
			public Long doInHibernate(Session session) throws HibernateException, SQLException {
				Criteria c = session.createCriteria(Variante.class);
				Disjunction or = Restrictions.disjunction();
				or.add(Restrictions.like("nombre","%" +param+ "%" ));
				or.add(Restrictions.like("p.nombre","%" +param+ "%"));
				or.add(Restrictions.like("descripcion","%" +param+ "%"));
				c.createAlias("producto", "p")
				 .createAlias("p.categoria", "c")
				 .createAlias("c.vendedor", "v")
				 .add(Restrictions.eq("p.ocultado", false))
				 .add(Restrictions.eq("v.id", idVendedor))
				 .add(or)
				 .setProjection(Projections.rowCount());
				return  (Long) c.uniqueResult();
			}
		});
	}

	@Override
	public List<Variante> obtenerDestacadosPorVendedor(final Integer idVendedor) {
//		final Integer inicio = calcularInicio(pagina,cantItems);
//		final Integer fin = calcularFin(pagina,cantItems);
		return this.getHibernateTemplate().executeFind(new HibernateCallback<List<Variante>>() {

			@Override
			public List<Variante> doInHibernate(Session session) throws HibernateException, SQLException {
				Criteria c = session.createCriteria(Variante.class);
				c.createAlias("producto", "p")
				 .createAlias("p.categoria", "c")
				 .createAlias("c.vendedor", "v")
				 .add(Restrictions.eq("p.ocultado", false))
				 .add(Restrictions.eq("v.id", idVendedor))
				 .add(Restrictions.eq("destacado", true))
				 .addOrder(Order.asc("p.nombre"));
//				 .createAlias("p.caracteristicas", "m")
//				 .add(Restrictions.eq("m.id", idMedalla))
//				 .setFirstResult(inicio)
//				 .setMaxResults(cantItems)
//				 .addOrder(Order.asc("id"));
				return  (List<Variante>)c.list();
			}
		});
	}
	
	@Override
	public Long totalVariantesSinFiltro(final Integer idVendedor) {
		return this.getHibernateTemplate().execute(new HibernateCallback<Long>() {

			@Override
			public Long doInHibernate(Session session) throws HibernateException, SQLException {
				Criteria c = session.createCriteria(Variante.class);
				c.createAlias("producto", "p")
				 .createAlias("p.fabricante", "f")
				 .add(Restrictions.eq("f.idVendedor", idVendedor))
				 .add(Restrictions.eq("p.ocultado", false))
				 .setProjection(Projections.rowCount());				
				return  (Long)c.uniqueResult();
			}
		});
	}

	@Override
	public List<Variante> obtenerProductosConMedallaEnProductor(final Integer medallaId) {
		return this.getHibernateTemplate().executeFind(new HibernateCallback<List<Variante>>() {

			@Override
			public List<Variante> doInHibernate(Session session) throws HibernateException, SQLException {
				Criteria c = session.createCriteria(Variante.class);
				c.createAlias("producto", "p")
				 .createAlias("p.fabricante", "f")
				 .createAlias("f.caracteristica", "c")
				 .add(Restrictions.eq("p.ocultado", false))
				 .add(Restrictions.eq("c.id", medallaId))
				//OCULTADO .add(Restrictions.eq("p.ocultado", false))
				 .addOrder(Order.asc("p.nombre"));
				return  (List<Variante>)c.list();
			}
		});
	}

	@Override
	public void eliminarVariante(Variante variante) {
		this.getHibernateTemplate().delete(variante);
		this.getHibernateTemplate().flush();
		
	}

	@Override
	public void eliminarProducto(Producto producto) {
		this.getHibernateTemplate().delete(producto);
		this.getHibernateTemplate().flush();
	}


}
