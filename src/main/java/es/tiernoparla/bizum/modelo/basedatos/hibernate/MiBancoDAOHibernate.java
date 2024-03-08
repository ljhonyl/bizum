package es.tiernoparla.bizum.modelo.basedatos.hibernate;

import es.tiernoparla.bizum.modelo.CuentaBancaria;
import es.tiernoparla.bizum.modelo.CuentaUsuario;
import es.tiernoparla.bizum.modelo.basedatos.IMiBancoDAO;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import java.util.ArrayList;
import java.util.List;

public class MiBancoDAOHibernate implements IMiBancoDAO {

    public MiBancoDAOHibernate(){

    }
    @Override
    public boolean agregarCuentaUsuario(CuentaUsuario usuario) {
        boolean exito=false;
        Session session = HibernateUti.getSessionFactory().openSession();
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
        }
        return exito;
    }

    @Override
    public boolean agregarCuentaBancaria(int cuentaUsuario, double saldo) {
        boolean exito=false;
        Session session= HibernateUti.getSessionFactory().openSession();
        Transaction transaction=session.beginTransaction();
        try {
            CuentaBancaria cuentaBancaria=new CuentaBancaria(saldo,cuentaUsuario);
            session.save(cuentaBancaria);
            transaction.commit();
            exito = true;
        }
        catch (Exception e) {
            e.printStackTrace();
            transaction.rollback();
        }
        finally {
            session.close();
        }
        return exito;
    }

    @Override
    public void ingresar(int numCuenta, double cantidad) {
        final String QUERY = "UPDATE CuentaBancaria SET saldo = saldo + :cantidad WHERE numCuenta = :numCuenta";
        realizarOperacion(numCuenta,cantidad,QUERY);
    }

    @Override
    public void retirar(int numCuenta, double cantidad) {
        final String QUERY = "UPDATE CuentaBancaria SET saldo = saldo - :cantidad WHERE numCuenta = :numCuenta";
        realizarOperacion(numCuenta,cantidad,QUERY);
    }

    @Override
    public int hacerBizum(int idUsuario, int telefono, double cantidad) {
        int exito = -1;
        int numCuentaEmisor = buscarCuentaConBizum(idUsuario);
        int numCuentaReceptor = buscarCuentaBizumPorNumero(telefono);

        if (numCuentaEmisor != -1 && numCuentaReceptor != -1) {
            double saldo = comprobarSaldoCuenta(numCuentaEmisor);

            if (saldo >= cantidad) {
                retirar(numCuentaEmisor, cantidad);
                ingresar(numCuentaReceptor, cantidad);
                exito = 1;
            } else {
                exito = 0;
            }
        }

        return exito;
    }

    @Override
    public List<CuentaBancaria> getCuentasBancarias(int idUsuario) {
        List<CuentaBancaria> cuentas = new ArrayList<>();
        int cuentaBizum = buscarCuentaConBizum(idUsuario);

        try (Session session = HibernateUti.getSessionFactory().openSession()) {
            Query<CuentaBancaria> query = session.createQuery("SELECT cb FROM CuentaBancaria cb WHERE cb.cuentaUsuario.id = :idUsuario", CuentaBancaria.class);
            query.setParameter("idUsuario", idUsuario);
            cuentas = query.list();
            for (CuentaBancaria cuenta : cuentas) {
                if (cuenta.getNumCuenta() == cuentaBizum) {
                    cuenta.setEsBizum(true);
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cuentas;
    }

    private int buscarCuentaConBizum(int idUsuario) {
        int cuentaBizum=-1;
        final String QUERRY="SELECT cuentaBizum FROM CuentaUsuario  WHERE id = :idUsuario";
        try (Session session = HibernateUti.getSessionFactory().openSession()) {
            // Consulta HQL para obtener el valor de CuentaBizum para un Id de usuario específico
            Query<CuentaBancaria> query = session.createQuery(QUERRY, CuentaBancaria.class);
            query.setParameter("idUsuario", idUsuario);

            // Obtener el resultado de la consulta
            CuentaBancaria cuentaBancaria=query.uniqueResult();
            if (cuentaBancaria != null) {
                cuentaBizum = cuentaBancaria.getNumCuenta();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cuentaBizum;
    }

    @Override
    public List<String> comprobarContrasena(String dni) {
        List<String> datos = new ArrayList<>();
        String consulta="SELECT id, contrasena FROM CuentaUsuario WHERE dni  = :dni";

        try(Session session = HibernateUti.getSessionFactory().openSession()){
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
        }
        return datos;
    }

    @Override
    public void seleccionarCuentaBizum(int numeroCuenta, int idUsuario) {
        try (Session session = HibernateUti.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                // Obtener la entidad CuentaUsuario a través de su identificador único
                CuentaUsuario cuentaUsuario = session.get(CuentaUsuario.class, idUsuario);
                // Actualizar el campo CuentaBizum de la entidad
                cuentaUsuario.setCuentaBizum(new CuentaBancaria(numeroCuenta));
                // Guardar los cambios en la base de datos
                session.update(cuentaUsuario);
                transaction.commit();
            } catch (Exception e) {
                if (transaction != null) {
                    transaction.rollback();
                }
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getNombreBeneficiario(int telefono) {
        final String QUERY = "SELECT nombre, apellidos FROM CuentaUsuario WHERE telefono=:telefono";
        String nombre = "";

        try (Session session = HibernateUti.getSessionFactory().openSession()) {
            Query query = session.createQuery(QUERY);
            query.setParameter("telefono", telefono);

            Object[] result = (Object[]) query.uniqueResult();

            if (result != null) {
                nombre = result[0].toString() + " " + result[1].toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return nombre;
    }

    private void realizarOperacion(int numCuenta, double cantidad, String QUERY){
        try (Session session = HibernateUti.getSessionFactory().openSession()) {
            Transaction transaction = null;
            try {
                transaction = session.beginTransaction();

                // Ejecutar la consulta con Hibernate
                session.createQuery(QUERY)
                        .setParameter("cantidad", cantidad)
                        .setParameter("numCuenta", numCuenta)
                        .executeUpdate();

                // Confirmar la transacción
                transaction.commit();
            } catch (Exception e) {
                if (transaction != null) {
                    transaction.rollback();
                }
                e.printStackTrace();
            }
        }
    }

    private int buscarCuentaBizumPorNumero(int telefono) {
        final String QUERY = "SELECT cu.cuentaBizum FROM CuentaUsuario cu WHERE cu.telefono = :telefono";
        int cuentaBizum = -1;
        try (Session session = HibernateUti.getSessionFactory().openSession()) {
            Query<CuentaBancaria> query = session.createQuery(QUERY);
            query.setParameter("telefono", telefono);
            CuentaBancaria cuentaBancaria = query.uniqueResult();
            if (cuentaBancaria != null) {
                cuentaBizum = cuentaBancaria.getNumCuenta();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cuentaBizum;
    }

    private double comprobarSaldoCuenta(int numCuentaEmisor) {
        final String QUERY = "SELECT cb.saldo FROM CuentaBancaria cb WHERE cb.numCuenta = :numCuentaEmisor";
        double saldo = -1;
        try (Session session = HibernateUti.getSessionFactory().openSession()) {
            Query<Double> query = session.createQuery(QUERY, Double.class);
            query.setParameter("numCuentaEmisor", numCuentaEmisor);
            saldo = query.uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return saldo;
    }
}
