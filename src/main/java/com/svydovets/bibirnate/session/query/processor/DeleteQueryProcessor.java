package com.svydovets.bibirnate.session.query.processor;

import com.svydovets.bibirnate.session.query.CascadeType;

public class DeleteQueryProcessor extends QueryProcessor {

    public DeleteQueryProcessor(Object entity) {
        super(entity);
    }

    @Override
    public CascadeType getOperationCascadeType() {
        return CascadeType.DELETE;
    }

    @Override
    public String generateQuery() {
        return null;
    }

    @Override
    public void execute() {

        if (hasToManyRelations()){
//            todo: handle OneToMany biDir + uniDir
        }

        if (hasToOneRelations()){
//            todo: handle
//                      OneToOne biDir + uniDir
//                      ManyToOne biDir + uniDir
        }

//        DELETE FROM TABLE_NAME WHERE ID = :id

    }

    private
}
