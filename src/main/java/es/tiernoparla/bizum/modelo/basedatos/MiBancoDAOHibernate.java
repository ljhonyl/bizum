package es.tiernoparla.bizum.modelo.basedatos;

import es.tiernoparla.bizum.modelo.CuentaBancaria;
import es.tiernoparla.bizum.modelo.CuentaUsuario;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.query.Query;

import java.util.ArrayList;
import java.util.List;

public class MiBancoDAOHibernate implements IMiBancoDAO{

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
        return null;
    }

    @Override
    public List<String> comprobarContrasena(String dni) {
        List<String> datos = new ArrayList<>();

        StandardServiceRegistry ssr = new StandardServiceRegistryBuilder()
                .configure("hibernate.cfg.xml").build();
        SessionFactory factory = new MetadataSources(ssr)
                .buildMetadata().buildSessionFactory();
        Session session = factory.openSession();

        try {
            Query query = session.createQuery("SELECT Id, Contrasena FROM CuentaUsuario WHERE Dni = :dni");
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
