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
import { IAufgabe } from 'app/shared/model/aufgabe.model';
import { Kategorie } from 'app/shared/model/enumerations/kategorie.model';
import { Level } from 'app/shared/model/enumerations/level.model';
import { getEntity, updateEntity, createEntity, reset } from './aufgabe.reducer';

export const AufgabeUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const spielers = useAppSelector(state => state.spieler.entities);
  const aufgabeEntity = useAppSelector(state => state.aufgabe.entity);
  const loading = useAppSelector(state => state.aufgabe.loading);
  const updating = useAppSelector(state => state.aufgabe.updating);
  const updateSuccess = useAppSelector(state => state.aufgabe.updateSuccess);
  const kategorieValues = Object.keys(Kategorie);
  const levelValues = Object.keys(Level);
  const handleClose = () => {
    props.history.push('/aufgabe' + props.location.search);
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
      ...aufgabeEntity,
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
          kategorie: 'XXX',
          level: 'EASY',
          ...aufgabeEntity,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="drinkGameApp.aufgabe.home.createOrEditLabel" data-cy="AufgabeCreateUpdateHeading">
            Create or edit a Aufgabe
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? <ValidatedField name="id" required readOnly id="aufgabe-id" label="ID" validate={{ required: true }} /> : null}
              <ValidatedField
                label="Name"
                id="aufgabe-name"
                name="name"
                data-cy="name"
                type="text"
                validate={{
                  required: { value: true, message: 'This field is required.' },
                }}
              />
              <ValidatedField
                label="Kurztext"
                id="aufgabe-kurztext"
                name="kurztext"
                data-cy="kurztext"
                type="text"
                validate={{
                  required: { value: true, message: 'This field is required.' },
                }}
              />
              <ValidatedField
                label="Beschreibung"
                id="aufgabe-beschreibung"
                name="beschreibung"
                data-cy="beschreibung"
                type="text"
                validate={{
                  required: { value: true, message: 'This field is required.' },
                }}
              />
              <ValidatedField label="Kategorie" id="aufgabe-kategorie" name="kategorie" data-cy="kategorie" type="select">
                {kategorieValues.map(kategorie => (
                  <option value={kategorie} key={kategorie}>
                    {kategorie}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField label="Level" id="aufgabe-level" name="level" data-cy="level" type="select">
                {levelValues.map(level => (
                  <option value={level} key={level}>
                    {level}
                  </option>
                ))}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/aufgabe" replace color="info">
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

export default AufgabeUpdate;
