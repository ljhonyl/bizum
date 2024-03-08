package es.tiernoparla.bizum.modelo.basedatos.hibernate;

import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class HibernateUti {

    private static SessionFactory sessionFactory;

    static {
        try {
            StandardServiceRegistry standardRegistry = new StandardServiceRegistryBuilder().configure("hibernate.cfg.xml").build();
            // Crear el objeto MetadataSources
            MetadataSources metadataSources = new MetadataSources(standardRegistry);
            // Crear el objeto Metadata usando las fuentes de metadatos
            Metadata metadata = metadataSources.getMetadataBuilder().build();
            // Construir la sesión de fábrica
            sessionFactory = metadata.getSessionFactoryBuilder().build();
        } catch (Exception e) {
            e.printStackTrace();
            // En un entorno real, probablemente querrás manejar esta excepción de manera diferente.
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public static void shutdown() {
        // Cerrar los recursos de Hibernate
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }
}
