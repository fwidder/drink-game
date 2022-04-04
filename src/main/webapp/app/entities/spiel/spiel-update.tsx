import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IBenutzer } from 'app/shared/model/benutzer.model';
import { getEntities as getBenutzers } from 'app/entities/benutzer/benutzer.reducer';
import { ISpiel } from 'app/shared/model/spiel.model';
import { getEntity, updateEntity, createEntity, reset } from './spiel.reducer';

export const SpielUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const benutzers = useAppSelector(state => state.benutzer.entities);
  const spielEntity = useAppSelector(state => state.spiel.entity);
  const loading = useAppSelector(state => state.spiel.loading);
  const updating = useAppSelector(state => state.spiel.updating);
  const updateSuccess = useAppSelector(state => state.spiel.updateSuccess);
  const handleClose = () => {
    props.history.push('/spiel' + props.location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(props.match.params.id));
    }

    dispatch(getBenutzers({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...spielEntity,
      ...values,
      benutzer: benutzers.find(it => it.id.toString() === values.benutzer.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          ...spielEntity,
          benutzer: spielEntity?.benutzer?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="drinkGameApp.spiel.home.createOrEditLabel" data-cy="SpielCreateUpdateHeading">
            Create or edit a Spiel
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? <ValidatedField name="id" required readOnly id="spiel-id" label="ID" validate={{ required: true }} /> : null}
              <ValidatedField
                label="Name"
                id="spiel-name"
                name="name"
                data-cy="name"
                type="text"
                validate={{
                  required: { value: true, message: 'This field is required.' },
                }}
              />
              <ValidatedField id="spiel-benutzer" name="benutzer" data-cy="benutzer" label="Benutzer" type="select" required>
                <option value="" key="0" />
                {benutzers
                  ? benutzers.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <FormText>This field is required.</FormText>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/spiel" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">Back</span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp; Save
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default SpielUpdate;
