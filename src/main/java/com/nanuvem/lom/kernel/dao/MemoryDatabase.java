package com.nanuvem.lom.kernel.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import com.nanuvem.lom.api.PropertyType;
import com.nanuvem.lom.api.Property;
import com.nanuvem.lom.api.EntityType;
import com.nanuvem.lom.api.Entity;

public class MemoryDatabase {

	private HashMap<Long, EntityType> entitiesById = new HashMap<Long, EntityType>();
	private HashMap<Long, List<Entity>> instancesByEntityId = new HashMap<Long, List<Entity>>();

	public void addEntity(EntityType entity) {
		entitiesById.put(entity.getId(), entity);
	}

	public Collection<EntityType> getEntities() {
		return entitiesById.values();
	}

	public void updateEntity(EntityType entity) {
		EntityType myEntity = findEntityById(entity.getId());

		myEntity.setName(entity.getName());
		myEntity.setNamespace(entity.getNamespace());
		myEntity.setVersion(entity.getVersion());
	}

	public void deleteEntity(Long id) {
		entitiesById.remove(id);
	}

	public EntityType findEntityByFullName(String fullName) {
		Collection<EntityType> values = entitiesById.values();
		for (EntityType entity : values) {
			if (entity.getFullName().equalsIgnoreCase(fullName)) {
				return entity;
			}
		}

		return null;
	}

	public EntityType findEntityById(Long id) {
		return entitiesById.get(id);
	}

	public void addAttribute(PropertyType attribute) {
		EntityType entity = findEntityById(attribute.getEntityType().getId());
		shiftSequence(attribute, entity);
		attribute.setEntityType(entity);
	}

	public PropertyType updateAtribute(PropertyType attribute) {
		EntityType entity = findEntityById(attribute.getEntityType().getId());

		PropertyType attributeInEntity = null;
		boolean changeSequence = false;

		for (int i = 0; i < entity.getPropertiesTypes().size(); i++) {
			attributeInEntity = entity.getPropertiesTypes().get(i);
			if (attribute.getId().equals(attributeInEntity.getId())) {

				if (!attribute.getSequence().equals(
						attributeInEntity.getSequence())) {
					changeSequence = true;
				}

				attributeInEntity.setName(attribute.getName());
				attributeInEntity.setType(attribute.getType());
				attributeInEntity
						.setConfiguration(attribute.getConfiguration());
				attributeInEntity
						.setVersion(attributeInEntity.getVersion() + 1);
				break;
			}
		}

		if (changeSequence) {
			PropertyType temp = null;
			for (PropertyType at : entity.getPropertiesTypes()) {
				if (attribute.getId().equals(at.getId())) {
					temp = at;
					entity.getPropertiesTypes().remove(at);
					temp.setSequence(attribute.getSequence());

					this.shiftSequence(temp, entity);
					break;
				}
			}
		}

		return attributeInEntity;
	}

	private void shiftSequence(PropertyType attribute, EntityType entity) {
		int i = 0;
		for (; i < entity.getPropertiesTypes().size(); i++) {
			if (attribute.getSequence().equals(
					entity.getPropertiesTypes().get(i).getSequence())) {
				break;
			}
		}

		i++;
		entity.getPropertiesTypes().add(i - 1, attribute);

		for (; i < entity.getPropertiesTypes().size(); i++) {
			PropertyType nextAttribute = null;
			try {
				nextAttribute = entity.getPropertiesTypes().get(i);
			} catch (IndexOutOfBoundsException e) {
				break;
			}

			if (nextAttribute.getSequence().equals(
					entity.getPropertiesTypes().get(i - 1).getSequence())) {
				nextAttribute.setSequence(nextAttribute.getSequence() + 1);
			}
		}
	}

	public void addInstance(Entity instance) {
		EntityType entity = findEntityById(instance.getEntityType().getId());
		instance.setEntityType(entity);
		getInstances(entity.getId()).add(instance);
	}

	public void updateInstance(Entity instance) {
		EntityType entity = findEntityById(instance.getEntityType().getId());
		List<Entity> instances = getInstances(entity.getId());

		for (int i = 0; i < instances.size(); i++) {
			if (instances.get(i).getId().equals(instance.getId())) {
				instances.remove(i);
				instances.add(i, instance);
				break;
			}
		}
	}

	public List<Entity> getInstances(Long idEntity) {
		if (instancesByEntityId.get(idEntity) == null) {
			instancesByEntityId.put(idEntity, new ArrayList<Entity>());
		}

		return instancesByEntityId.get(idEntity);
	}

	public void addAttributeValue(Property value) {
		Entity instance = findInstanceById(value.getEntity().getId());
		value.setEntity(instance);
		instance.getProperties().add(value);
	}

	private Entity findInstanceById(Long id) {
		for (EntityType entity : getEntities()) {
			for (Entity instance : getInstances(entity.getId())) {
				if (instance.getId().equals(id)) {
					return instance;
				}
			}
		}
		return null;
	}

	public Property updateAttributeValue(Property value) {
		Entity instance = findInstanceById(value.getEntity().getId());

		for (Property attributeValue : instance.getProperties()) {
			if (value.getId().equals(attributeValue.getId())) {
				attributeValue.setPropertyType(value.getPropertyType());
				attributeValue.setEntity(value.getEntity());
				attributeValue.setValue(value.getValue());
				attributeValue.setVersion(attributeValue.getVersion() + 1);

				return attributeValue;
			}
		}
		return null;
	}

}
