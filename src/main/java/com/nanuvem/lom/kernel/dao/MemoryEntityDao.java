package com.nanuvem.lom.kernel.dao;

import java.util.ArrayList;
import java.util.List;

import com.nanuvem.lom.api.EntityType;
import com.nanuvem.lom.api.MetadataException;
import com.nanuvem.lom.api.dao.EntityTypeDao;

public class MemoryEntityDao implements EntityTypeDao {

    private Long id = 1L;

    private MemoryDatabase memoryDatabase;

    public MemoryEntityDao(MemoryDatabase memoryDatabase) {
        this.memoryDatabase = memoryDatabase;
    }

    public EntityType create(EntityType entity) {
        entity.setId(id++);
        entity.setVersion(0);

        memoryDatabase.addEntity(entity);

        return entity;
    }

    public List<EntityType> listAll() {
        List<EntityType> classesReturn = new ArrayList<EntityType>();

        for (EntityType entity : memoryDatabase.getEntities()) {
            classesReturn.add(entity);
        }
        return classesReturn;
    }

    public EntityType update(EntityType entity) {
        for (EntityType e : memoryDatabase.getEntities()) {
            if (e.getId().equals(entity.getId())) {
                if (e.getVersion() > entity.getVersion()) {
                    throw new MetadataException("Updating a deprecated version of Entity " + e.getNamespace() + "."
                            + e.getName() + ". Get the Entity again to obtain the newest version and proceed updating.");
                }
                entity.setVersion(e.getVersion() + 1);
                memoryDatabase.updateEntity(entity);
                return entity;
            }
        }
        throw new MetadataException("Invalid id for Entity " + entity.getNamespace() + "." + entity.getName());
    }

    public EntityType findById(Long id) {
        for (EntityType e : memoryDatabase.getEntities()) {
            if (e.getId().equals(id)) {
                return e;
            }
        }
        return null;
    }

    public List<EntityType> listByFullName(String fragment) {
        List<EntityType> results = new ArrayList<EntityType>();
        for (EntityType e : memoryDatabase.getEntities()) {
            if (e.getNamespace().toLowerCase().contains(fragment.toLowerCase())
                    || e.getName().toLowerCase().contains(fragment.toLowerCase())
                    || e.getFullName().toLowerCase().contains(fragment.toLowerCase())) {
                results.add(e);
            }
        }
        return results;
    }

    public EntityType findByFullName(String fullName) {
        String namespace = null;
        String name = null;

        if (fullName.contains(".")) {
            namespace = fullName.substring(0, fullName.lastIndexOf("."));
            name = fullName.substring(fullName.lastIndexOf(".") + 1, fullName.length());
        } else {
            name = fullName;
        }

        for (EntityType entity : memoryDatabase.getEntities()) {
            if ((namespace + "." + name).equalsIgnoreCase(entity.getFullName())) {
                return entity;
            }
        }
        return null;
    }

    public void delete(Long id) {
        if (findById(id) == null) {
            throw new MetadataException("Unknown id for Entity: " + id);
        }

        memoryDatabase.deleteEntity(id);
    }
}
