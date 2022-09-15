package com.svydovets.bibirnate.session.state;

import com.svydovets.bibirnate.session.state.key.KeyEntity;
import com.svydovets.bibirnate.utils.EntityUtils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static com.svydovets.bibirnate.utils.EntityUtils.getFieldValue;
import static com.svydovets.bibirnate.utils.EntityUtils.getSortedFields;


/**
 * EntityStateContainer class container, the main responsibility is to keep the entities initial entity state,
 * after entity initialization, for future state validation and catch up the field modification.
 */
@Slf4j
public class EntityStateContainer {

    private final Map<KeyEntity, Object[]> entitiesState;

    public EntityStateContainer() {
        entitiesState = new ConcurrentHashMap<>();
    }

    public Set<Map.Entry<KeyEntity, Object[]>> getStateEntries() {
        return entitiesState.entrySet();
    }

    /**
     * Returns entity object fulfilled in current state;
     *
     * @param key which field should be stored.
     */
    @SneakyThrows
    public Object getEntityByKey(KeyEntity key) {
        var data = entitiesState.get(key);
        Objects.requireNonNull(data);
        var constructor = key.entityType().getConstructor();
        var instance = constructor.newInstance();
        var fields = instance.getClass().getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            fields[i].setAccessible(Boolean.TRUE);
            fields[i].set(instance, data[i]);
        }
        return instance;

    }



    public boolean isEntityStateConsistent(Object entity) {
        var key = KeyEntity.of(entity);
        var snapshotValues = entitiesState.get(key);
        var fields = getSortedFields(key.entityType());

        for (int i = 0; i < fields.length; i++) {
            if (getFieldValue(fields[i], entity) != snapshotValues[i]) {
                log.trace("Entity field value '" + entity + "' has been modified and should be updated on " + snapshotValues[i]);
                return false;
            }
        }
        return true;
    }

    /**
     * Put the entity to the map, transforming & sorting by Name the fields of the entity.
     *
     * @param entity which field should be stored.
     */
    public void put(Object entity) {
        var key = KeyEntity.of(entity);
        var fields = Arrays.stream(entity.getClass().getDeclaredFields())
                .peek(field -> field.setAccessible(Boolean.TRUE))
                .sorted(Comparator.comparing(Field::getName))
                .map(field -> getFieldValue(field, entity))
                .toArray();

        log.trace("Entity " + entity + " has been loaded with such values " + Arrays.toString(fields));
        entitiesState.put(key, fields);
    }

}
