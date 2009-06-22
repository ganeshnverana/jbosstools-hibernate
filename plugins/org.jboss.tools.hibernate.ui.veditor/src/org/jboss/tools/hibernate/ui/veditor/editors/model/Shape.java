/*******************************************************************************
 * Copyright (c) 2007 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.hibernate.ui.veditor.editors.model;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;
import org.hibernate.mapping.Column;
import org.hibernate.mapping.Component;
import org.hibernate.mapping.Property;
import org.hibernate.mapping.Value;
import org.jboss.tools.hibernate.ui.view.views.HibernateUtils;
import org.jboss.tools.hibernate.ui.view.views.OrmModelNameVisitor;

public class Shape extends ModelElement {
	
	private int indent = 0;
		
	private List<Connection> sourceConnections = new ArrayList<Connection>();
	private List<Connection> targetConnections = new ArrayList<Connection>();
	
	public static final String HIDE_SELECTION = "hide selection"; //$NON-NLS-1$
	public static final String SHOW_SELECTION = "show selection"; //$NON-NLS-1$
	public static final String SET_FOCUS = "set focus"; //$NON-NLS-1$
	
	private Object  ormElement;
	
	static OrmModelNameVisitor ormModelNameVisitor;
		
	private static IPropertyDescriptor[] descriptors_property;
	private static IPropertyDescriptor[] descriptors_column;

	/**
	 * Property set
	 */
	private static final String PROPERTY_NAME = "name"; //$NON-NLS-1$
	private static final String PROPERTY_TYPE = "type"; //$NON-NLS-1$
	private static final String PROPERTY_CLASS = "persistanceClass"; //$NON-NLS-1$
	private static final String PROPERTY_VALUE = "value"; //$NON-NLS-1$
	private static final String PROPERTY_SELECT = "selectable"; //$NON-NLS-1$
	private static final String PROPERTY_INSERT = "insertable"; //$NON-NLS-1$
	private static final String PROPERTY_UPDATE = "updateable"; //$NON-NLS-1$
	private static final String PROPERTY_CASCADE = "cascade"; //$NON-NLS-1$
	private static final String PROPERTY_LAZY = "lazy"; //$NON-NLS-1$
	private static final String PROPERTY_OPTIONAL = "optional"; //$NON-NLS-1$
	private static final String PROPERTY_NATURAL_IDENTIFIER = "naturalIdentifier"; //$NON-NLS-1$
	private static final String PROPERTY_NODE_NAME = "nodeName"; //$NON-NLS-1$
	private static final String PROPERTY_OPTIMISTIC_LOCKED = "optimisticLocked"; //$NON-NLS-1$
	private static final String PROPERTY_NULLABLE = "nullable"; //$NON-NLS-1$
	private static final String PROPERTY_UNIQUE = "unique"; //$NON-NLS-1$

	static {
		
		ormModelNameVisitor = new OrmModelNameVisitor();
		
		descriptors_property = new IPropertyDescriptor[] { 
				new TextPropertyDescriptor(PROPERTY_NAME, PROPERTY_NAME),
				new TextPropertyDescriptor(PROPERTY_TYPE, PROPERTY_TYPE),
				new TextPropertyDescriptor(PROPERTY_VALUE, PROPERTY_VALUE),
				new TextPropertyDescriptor(PROPERTY_CLASS, PROPERTY_CLASS),
				new TextPropertyDescriptor(PROPERTY_SELECT, PROPERTY_SELECT),
				new TextPropertyDescriptor(PROPERTY_INSERT, PROPERTY_INSERT),
				new TextPropertyDescriptor(PROPERTY_UPDATE, PROPERTY_UPDATE),
				new TextPropertyDescriptor(PROPERTY_CASCADE, PROPERTY_CASCADE),
				new TextPropertyDescriptor(PROPERTY_LAZY, PROPERTY_LAZY),
				new TextPropertyDescriptor(PROPERTY_OPTIONAL, PROPERTY_OPTIONAL),
				new TextPropertyDescriptor(PROPERTY_NATURAL_IDENTIFIER, PROPERTY_NATURAL_IDENTIFIER),
				new TextPropertyDescriptor(PROPERTY_NODE_NAME, PROPERTY_NODE_NAME),
				new TextPropertyDescriptor(PROPERTY_OPTIMISTIC_LOCKED, PROPERTY_OPTIMISTIC_LOCKED),
		};

	
		descriptors_column = new IPropertyDescriptor[] { 
				new TextPropertyDescriptor(PROPERTY_NAME, PROPERTY_NAME),
				new TextPropertyDescriptor(PROPERTY_TYPE, PROPERTY_TYPE),
				new TextPropertyDescriptor(PROPERTY_VALUE, PROPERTY_VALUE),
				new TextPropertyDescriptor(PROPERTY_NULLABLE, PROPERTY_NULLABLE),
				new TextPropertyDescriptor(PROPERTY_UNIQUE, PROPERTY_UNIQUE),
		};

	} // static

	protected Shape(Object ioe) {
		ormElement = ioe;
	}

	private ModelElement parent;
	
	public ModelElement getParent() {
		return parent;
	}
	
	public void setParent(ModelElement element) {
		parent = element;
	}
	
	public OrmDiagram getOrmDiagram() {
		OrmDiagram res = null;
		ModelElement el = this;
		while (el != null) {
			if (el instanceof OrmDiagram) {
				res = (OrmDiagram)el;
				break;
			}
			el = el.getParent();
		}
		return res;
	}
	
	public ExpandeableShape getExtendeableShape() {
		ExpandeableShape res = null;
		ModelElement el = this;
		while (el != null) {
			if (el instanceof ExpandeableShape) {
				res = (ExpandeableShape)el;
				break;
			}
			el = el.getParent();
		}
		return res;
	}
	
	public OrmShape getOrmShape() {
		OrmShape res = null;
		ModelElement el = this;
		while (el != null) {
			if (el instanceof OrmShape) {
				res = (OrmShape)el;
				break;
			}
			el = el.getParent();
		}
		return res;
	}

	public void addConnection(Connection conn) {
		if (conn == null || conn.getSource() == conn.getTarget()) {
			throw new IllegalArgumentException();
		}
		if (conn.getSource() == this) {
			sourceConnections.add(conn);
		} else if (conn.getTarget() == this) {
			targetConnections.add(conn);
		}
	}	
	
	public List<Connection> getSourceConnections() {
		return sourceConnections;
	}
	
	public List<Connection> getTargetConnections() {
		return targetConnections;
	}
	
	public Object getOrmElement() {
		return ormElement;
	}
	
	public void hideSelection() {
		firePropertyChange(HIDE_SELECTION, null, null);
	}

	public void showSelection() {
		firePropertyChange(SHOW_SELECTION, null, null);
	}

	public void setFocus() {
		firePropertyChange(SET_FOCUS, null, null);		
	}
	
	public int getIndent() {
		return indent;
	}

	protected void setIndent(int indent) {
		this.indent = indent;
	}
	
	protected void setHidden(boolean hiden) {
		for (int i = 0; i < sourceConnections.size(); i++) {
			sourceConnections.get(i).setHidden(hiden);
		}
		for (int i = 0; i < targetConnections.size(); i++) {
			targetConnections.get(i).setHidden(hiden);
		}
	}

	/**
	 * Returns an array of IPropertyDescriptors for this shape.
	 * <p>The returned array is used to fill the property view, when the edit-part corresponding
	 * to this model element is selected.</p>
	 * @see #descriptors
	 * @see #getPropertyValue(Object)
	 * @see #setPropertyValue(Object, Object)
	 */
	public IPropertyDescriptor[] getPropertyDescriptors() {
		if (getOrmElement() instanceof Property) {
			return descriptors_property;
		}
		else if (getOrmElement() instanceof Column) {
			return descriptors_column;
		}
		return super.getPropertyDescriptors();
	}

	/**
	 * Return the property value for the given propertyId, or null.
	 * <p>The property view uses the IDs from the IPropertyDescriptors array 
	 * to obtain the value of the corresponding properties.</p>
	 * @see #descriptors
	 * @see #getPropertyDescriptors()
	 */
	public Object getPropertyValue(Object propertyId) {
		Object res = null;
		Column col = null;
		if (getOrmElement() instanceof Column) {
			col = (Column)getOrmElement();
		}
		Property prop = null;
		if (getOrmElement() instanceof Property) {
			prop = (Property)getOrmElement();
		}
		if (PROPERTY_NAME.equals(propertyId)) {
			if (prop != null) {
				res = prop.getName();
			}
			else if (col != null) {
				res = col.getName();
			}
		}
		else if (PROPERTY_TYPE.equals(propertyId)) {
			if (prop != null) {
				Value value = prop.getValue();
				if (value instanceof Component) {
					res = prop.getValue().toString();
				}
				else {
					res = prop.getType().getReturnedClass().getName();
				}
			}
			else if (col != null) {
				String type = ormModelNameVisitor.getColumnSqlType(col, getOrmDiagram().getConsoleConfig());

				StringBuffer name = new StringBuffer();

				if (type != null) {
					name.append(type.toUpperCase());
					name.append(HibernateUtils.getTable(col) != null
							&& HibernateUtils.isPrimaryKey(col) ? " PK" : ""); //$NON-NLS-1$  //$NON-NLS-2$
					name.append(HibernateUtils.getTable(col) != null
							&& HibernateUtils.isForeignKey(col) ? " FK" : ""); //$NON-NLS-1$ //$NON-NLS-2$
				}

				res = name.toString();
			}
		}
		else if (PROPERTY_VALUE.equals(propertyId)) {
			if (prop != null) {
				res = prop.getValue().toString();
			}
			else if (getOrmElement() instanceof Column) {
				res = col.getValue().toString();
			}
		}
		else if (PROPERTY_CLASS.equals(propertyId)) {
			if (prop != null) {
				if (prop.getPersistentClass() != null) {
					res = prop.getPersistentClass().getClassName();
				}
			}
		}
		else if (PROPERTY_SELECT.equals(propertyId)) {
			if (prop != null) {
				res = Boolean.valueOf(prop.isSelectable()).toString(); 
			}
		}
		else if (PROPERTY_INSERT.equals(propertyId)) {
			if (prop != null) {
				res = Boolean.valueOf(prop.isInsertable()).toString(); 
			}
		}
		else if (PROPERTY_UPDATE.equals(propertyId)) {
			if (prop != null) {
				res = Boolean.valueOf(prop.isUpdateable()).toString(); 
			}
		}
		else if (PROPERTY_CASCADE.equals(propertyId)) {
			if (prop != null) {
				res = prop.getCascade(); 
			}
		}
		else if (PROPERTY_LAZY.equals(propertyId)) {
			if (prop != null) {
				res = Boolean.valueOf(prop.isLazy()).toString(); 
			}
		}
		else if (PROPERTY_OPTIONAL.equals(propertyId)) {
			if (prop != null) {
				res = Boolean.valueOf(prop.isOptional()).toString(); 
			}
		}
		else if (PROPERTY_NATURAL_IDENTIFIER.equals(propertyId)) {
			if (prop != null) {
				res = Boolean.valueOf(prop.isNaturalIdentifier()).toString(); 
			}
		}
		else if (PROPERTY_NODE_NAME.equals(propertyId)) {
			if (prop != null) {
				res = prop.getNodeName();
			}
		}
		else if (PROPERTY_OPTIMISTIC_LOCKED.equals(propertyId)) {
			if (prop != null) {
				res = Boolean.valueOf(prop.isOptimisticLocked()).toString(); 
			}
		}
		else if (PROPERTY_NULLABLE.equals(propertyId)) {
			if (col != null) {
				res = Boolean.valueOf(col.isNullable()).toString(); 
			}
		}
		else if (PROPERTY_UNIQUE.equals(propertyId)) {
			if (col != null) {
				res = Boolean.valueOf(col.isUnique()).toString(); 
			}
		}
		if (res == null) {
			res = super.getPropertyValue(propertyId);
		}
		return toEmptyStr(res);
	}
	
	protected Object toEmptyStr(Object obj) {
		if (obj == null) {
			return ""; //$NON-NLS-1$
		}
		return obj;
	}

}
