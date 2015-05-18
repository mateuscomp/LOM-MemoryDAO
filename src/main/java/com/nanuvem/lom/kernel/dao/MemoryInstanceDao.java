package com.nanuvem.lom.kernel.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang.SerializationUtils;

import com.nanuvem.lom.api.EntityType;
import com.nanuvem.lom.api.Entity;
import com.nanuvem.lom.api.dao.EntityDao;

public class MemoryInstanceDao implements EntityDao {

	private Long id = 1L;
	private MemoryDatabase memoryDatabase;

	public MemoryInstanceDao(MemoryDatabase memoryDatabase) {
		this.memoryDatabase = memoryDatabase;
	}

	public Entity create(Entity instance) {
		instance.setId(id++);
		instance.setVersion(0);

		memoryDatabase.addInstance(instance);
		
		return instance;
	}

	public List<Entity> listAllInstances(String fullEntityName) {
		EntityType entity = memoryDatabase.findEntityByFullName(fullEntityName);
		
		List<Entity> cloneInstances = new ArrayList<Entity>();
		for (Entity it : memoryDatabase.getInstances(entity.getId())) {
			cloneInstances.add((Entity) SerializationUtils.clone(it));
		}
		return cloneInstances;
	}

	public Entity findInstanceById(Long id) {
		Collection<EntityType> entities = memoryDatabase.getEntities();

		for (EntityType entity : entities) {
			for (Entity instanceEach : memoryDatabase.getInstances(entity.getId())) {
				if (instanceEach.getId().equals(id)) {
					return instanceEach;
				}
			}
		}
		return null;
	}

    public List<Entity> findInstancesByEntityId(Long entityId) {
        return memoryDatabase.getInstances(entityId);
    }

	public Entity update(Entity instance) {
		instance.setVersion((instance.getVersion() + 1));
		memoryDatabase.updateInstance(instance);
		return instance;		
	}

	public void delete(Long id) {
		// TODO Auto-generated method stub
		
	}

}