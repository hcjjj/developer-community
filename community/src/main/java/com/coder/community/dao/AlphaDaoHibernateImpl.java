package com.coder.community.dao;

import org.springframework.stereotype.Repository;

@Deprecated
@Repository("alphaHibernate")
public class AlphaDaoHibernateImpl implements AlphaDao{
    @Override
    public String select() {
        return "Hibernate";
    }
}
