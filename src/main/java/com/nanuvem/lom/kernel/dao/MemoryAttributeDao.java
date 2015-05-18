package com.nanuvem.lom.kernel.dao;

import java.util.Collection;
import java.util.List;

import com.nanuvem.lom.api.PropertyType;
import com.nanuvem.lom.api.EntityType;
import com.nanuvem.lom.api.dao.PropertyTypeDao;

public class MemoryAttributeDao implements PropertyTypeDao {

	private Long id = 1L;
	private MemoryDatabase memoryDatabase;

	public MemoryAttributeDao(MemoryDatabase memoryDatabase) {
		this.memoryDatabase = memoryDatabase;
	}

	public PropertyType create(PropertyType attribute) {
		attribute.setId(id++);
		attribute.setVersion(0);

		memoryDatabase.addAttribute(attribute);

		return attribute;
	}

	public PropertyType findPropertyTypeById(Long id) {
		Collection<EntityType> entities = memoryDatabase.getEntities();

		for (EntityType entityEach : entities) {
			for (PropertyType attributeEach : entityEach.getPropertiesTypes()) {
				if (attributeEach.getId().equals(id)) {
					return attributeEach;
				}
			}
		}
		return null;
	}

	public PropertyType findPropertyTypeByNameAndEntityTypeFullName(String nameAttribute,
			String entityFullName) {

		EntityType entity = memoryDatabase.findEntityByFullName(entityFullName);
		if (entity.getPropertiesTypes() != null) {
			for (PropertyType attribute : entity.getPropertiesTypes()) {
				if (attribute.getName().equalsIgnoreCase(nameAttribute)) {
					return attribute;
				}
			}
		}
		return null;
	}

	public PropertyType update(PropertyType attribute) {
		return memoryDatabase.updateAtribute(attribute);
	}

	public List<PropertyType> findPropertiesTypesByFullNameEntityType(String fullnameEntity) {
		EntityType entity = memoryDatabase.findEntityByFullName(fullnameEntity);
		if (entity != null && entity.getPropertiesTypes() != null) {
			return entity.getPropertiesTypes();
		}
		return null;
	}
}