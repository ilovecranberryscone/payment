package com.payment.util;

import java.io.Serializable;
import java.text.SimpleDateFormat;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;
import org.hibernate.query.Query;
import org.hibernate.type.StandardBasicTypes;

public class ManagementNumberGenerator implements IdentifierGenerator{

	public Serializable generate(SharedSessionContractImplementor session, Object object) throws HibernateException{
		String key = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(System.currentTimeMillis());
		Query query = session.createSQLQuery("select MYSEQ.nextval as num from dual").addScalar("num", StandardBasicTypes.INTEGER);
		int result = (Integer)query.uniqueResult() % 999;
		String resultString = key + "";
		if(result < 10) {
			resultString = resultString + "00" + result;
		} else if(result < 100) {
			resultString = resultString + "0" + result;
		} else {
			resultString = resultString + result;
		}
		return resultString;
	}

}