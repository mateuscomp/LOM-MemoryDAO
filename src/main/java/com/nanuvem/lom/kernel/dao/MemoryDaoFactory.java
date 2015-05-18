package com.nanuvem.lom.kernel.dao;

import com.nanuvem.lom.api.dao.PropertyTypeDao;
import com.nanuvem.lom.api.dao.PropertyDao;
import com.nanuvem.lom.api.dao.DaoFactory;
import com.nanuvem.lom.api.dao.EntityTypeDao;
import com.nanuvem.lom.api.dao.EntityDao;

public class MemoryDaoFactory implements DaoFactory {

	private MemoryDatabase memoryDatabase;

	private MemoryEntityDao entityDao;
	private MemoryAttributeDao attributeDao;
	private MemoryInstanceDao instanceDao;
	private MemoryAttributeValueDao attributeValueDao;

	public MemoryDaoFactory() {
		memoryDatabase = new MemoryDatabase();
	}

	public EntityTypeDao createEntityTypeDao() {
		if (entityDao == null) {
			this.entityDao = new MemoryEntityDao(memoryDatabase);
		}
		return this.entityDao;
	}

	public PropertyTypeDao createPropertyTypeDao() {
		if (attributeDao == null) {
			this.attributeDao = new MemoryAttributeDao(memoryDatabase);
		}
		return this.attributeDao;
	}

	public EntityDao createEntityDao() {
		if (instanceDao == null) {
			this.instanceDao = new MemoryInstanceDao(memoryDatabase);
		}
		return this.instanceDao;
	}

	public PropertyDao createPropertyDao() {
		if (attributeValueDao == null) {
			this.attributeValueDao = new MemoryAttributeValueDao(memoryDatabase);
		}
		return this.attributeValueDao;
	}

	public void createDatabaseSchema() {
		// TODO Auto-generated method stub
	}

	public void dropDatabaseSchema() {
		// TODO Auto-generated method stub
	}
}
