import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { ISpieler } from 'app/shared/model/spieler.model';
import { getEntities as getSpielers } from 'app/entities/spieler/spieler.reducer';
import { IGetraenk } from 'app/shared/model/getraenk.model';
import { Groesse } from 'app/shared/model/enumerations/groesse.model';
import { getEntity, updateEntity, createEntity, reset } from './getraenk.reducer';

export const GetraenkUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const spielers = useAppSelector(state => state.spieler.entities);
  const getraenkEntity = useAppSelector(state => state.getraenk.entity);
  const loading = useAppSelector(state => state.getraenk.loading);
  const updating = useAppSelector(state => state.getraenk.updating);
  const updateSuccess = useAppSelector(state => state.getraenk.updateSuccess);
  const groesseValues = Object.keys(Groesse);
  const handleClose = () => {
    props.history.push('/getraenk' + props.location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(props.match.params.id));
    }

    dispatch(getSpielers({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...getraenkEntity,
      ...values,
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
          groesse: 'SHOT',
          ...getraenkEntity,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="drinkGameApp.getraenk.home.createOrEditLabel" data-cy="GetraenkCreateUpdateHeading">
            Create or edit a Getraenk
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? <ValidatedField name="id" required readOnly id="getraenk-id" label="ID" validate={{ required: true }} /> : null}
              <ValidatedField
                label="Name"
                id="getraenk-name"
                name="name"
                data-cy="name"
                type="text"
                validate={{
                  required: { value: true, message: 'This field is required.' },
                }}
              />
              <ValidatedField label="Groesse" id="getraenk-groesse" name="groesse" data-cy="groesse" type="select">
                {groesseValues.map(groesse => (
                  <option value={groesse} key={groesse}>
                    {groesse}
                  </option>
                ))}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/getraenk" replace color="info">
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

export default GetraenkUpdate;
