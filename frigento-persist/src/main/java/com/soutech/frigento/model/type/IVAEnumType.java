package com.soutech.frigento.model.type;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.usertype.UserType;

import com.soutech.frigento.enums.IVAEnum;

/**
 * UserType que permmite almacenar IVAEnum como float.
 * @author dsouza
 */
public class IVAEnumType implements UserType {
	
	public int[] sqlTypes() {
		int[] typeList = { Types.FLOAT };
		return typeList;
	}

	@SuppressWarnings("rawtypes")
	public Class returnedClass() {
		return Boolean.class;
	}

	@Override
	public boolean equals(Object x, Object y) throws HibernateException {
		if(x == null || y == null)	return false;
		return ((IVAEnum) x).equals(y);
	}

	@Override
	public Object nullSafeGet(ResultSet rs, String[] names, SessionImplementor arg2, Object owner)
			throws HibernateException, SQLException {
		
		float valor = rs.getFloat(names[0]);
		
		try {
			IVAEnum iva = IVAEnum.valueOf(valor);
			return iva;
		} catch (java.util.NoSuchElementException e) {
			throw new HibernateException("Bad IVAEnum value in UserTyped: "
					+ valor, e);
		}
	}

	@Override
	public void nullSafeSet(PreparedStatement st, Object value, int index, SessionImplementor arg3)
			throws HibernateException, SQLException {
		
		IVAEnum iva;
		try {
			iva = (IVAEnum)value;
		} catch (Exception e) {
			throw new HibernateException("Bad IVAEnum value in UserTyped: "
					+ value, e);
		}
		st.setFloat(index, iva.getIva());
	}

	@Override
	public Object deepCopy(Object value) throws HibernateException {
		return value;
	}

	@Override
	public boolean isMutable() {
		return false;
	}

	@Override
	public Object assemble(Serializable arg0, Object arg1)
			throws HibernateException {
		return arg0;
	}

	@Override
	public Serializable disassemble(Object arg0) throws HibernateException {
		return (Serializable) arg0;
	}

	@Override
	public int hashCode(Object arg0) throws HibernateException {
		return arg0.hashCode();
	}

	@Override
	public Object replace(Object arg0, Object arg1, Object arg2)
			throws HibernateException {
		return arg0;
	}

}