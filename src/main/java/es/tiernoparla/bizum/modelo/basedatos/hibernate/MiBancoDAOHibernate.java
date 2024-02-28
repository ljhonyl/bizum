package es.tiernoparla.bizum.modelo.basedatos.hibernate;

import es.tiernoparla.bizum.modelo.CuentaBancaria;
import es.tiernoparla.bizum.modelo.CuentaUsuario;
import es.tiernoparla.bizum.modelo.basedatos.IMiBancoDAO;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.query.Query;

import java.util.ArrayList;
import java.util.List;

public class MiBancoDAOHibernate implements IMiBancoDAO {

    public MiBancoDAOHibernate(){

    }
    @Override
    public boolean agregarCuentaUsuario(CuentaUsuario usuario) {
        boolean exito=false;
        StandardServiceRegistry ssr = new StandardServiceRegistryBuilder().configure("hibernate.cfg.xml").build();
        SessionFactory factory = new MetadataSources(ssr).buildMetadata().buildSessionFactory();
        Session session = factory.openSession();
        Transaction transact = session.beginTransaction();
        try {
            session.save(usuario);
            transact.commit();
            exito = true;
        }
        catch (Exception e) {
            e.printStackTrace();
            transact.rollback();
        }
        finally {
            session.close();
            factory.close();
        }
        return exito;
    }

    @Override
    public boolean agregarCuentaBancaria(int cuentaUsuario, double saldo) {
        return false;
    }

    @Override
    public void ingresar(int numCuenta, double cantidad) {

    }

    @Override
    public void retirar(int numCuenta, double cantidad) {

    }

    @Override
    public int hacerBizum(int idUsuario, int telefono, double cantidad) {
        return 0;
    }

    @Override
    public List<CuentaBancaria> getCuentasBancarias(int idUsuario) {
        List<CuentaBancaria> cuentas = new ArrayList<>();
        try (Session session = HibernateUti.getSessionFactory().openSession()) {
            // Usamos parámetros nombrados en la consulta para evitar SQL Injection
            Query<CuentaBancaria> query = session.createQuery(
                    "SELECT cb FROM CuentaBancaria cb WHERE cb.cuentaUsuario.id = :idUsuario", CuentaBancaria.class);
            query.setParameter("idUsuario", idUsuario);
            cuentas = query.list();

            // Buscamos la cuenta con Bizum
            int cuentaBizum = buscarCuentaConBizum(idUsuario);
            for (CuentaBancaria cuenta : cuentas) {
                if (cuenta.getNumCuenta() == cuentaBizum) {
                    cuenta.setEsBizum(true);
                    break; // Ya hemos encontrado la cuenta con Bizum, podemos salir del bucle
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cuentas;
    }

    private int buscarCuentaConBizum(int idUsuario) {
        return -1;
    }

    @Override
    public List<String> comprobarContrasena(String dni) {
        List<String> datos = new ArrayList<>();
        String consulta="SELECT id, contrasena FROM CuentaUsuario WHERE dni  = :dni";

        StandardServiceRegistry ssr = new StandardServiceRegistryBuilder()
                .configure("hibernate.cfg.xml").build();
        SessionFactory factory = new MetadataSources(ssr)
                .buildMetadata().buildSessionFactory();
        Session session = factory.openSession();

        try {
            Query query = session.createQuery(consulta);
            query.setParameter("dni", dni);
            Object[] result = (Object[]) query.uniqueResult();

            if (result != null) {
                int idUsuario = (int) result[0];
                String password = (String) result[1];
                datos.add(String.valueOf(idUsuario));
                datos.add(password);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close();
            factory.close();
        }

        return datos;
    }

    @Override
    public void seleccionarCuentaBizum(int numeroCuenta, int idUsuario) {

    }

    @Override
    public String getNombreBeneficiario(int numero) {
        return null;
    }
}
