import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { ISpiel } from 'app/shared/model/spiel.model';
import { getEntities as getSpiels } from 'app/entities/spiel/spiel.reducer';
import { IGetraenk } from 'app/shared/model/getraenk.model';
import { getEntities as getGetraenks } from 'app/entities/getraenk/getraenk.reducer';
import { IAufgabe } from 'app/shared/model/aufgabe.model';
import { getEntities as getAufgabes } from 'app/entities/aufgabe/aufgabe.reducer';
import { ISpieler } from 'app/shared/model/spieler.model';
import { getEntity, updateEntity, createEntity, reset } from './spieler.reducer';

export const SpielerUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const spiels = useAppSelector(state => state.spiel.entities);
  const getraenks = useAppSelector(state => state.getraenk.entities);
  const aufgabes = useAppSelector(state => state.aufgabe.entities);
  const spielerEntity = useAppSelector(state => state.spieler.entity);
  const loading = useAppSelector(state => state.spieler.loading);
  const updating = useAppSelector(state => state.spieler.updating);
  const updateSuccess = useAppSelector(state => state.spieler.updateSuccess);
  const handleClose = () => {
    props.history.push('/spieler' + props.location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(props.match.params.id));
    }

    dispatch(getSpiels({}));
    dispatch(getGetraenks({}));
    dispatch(getAufgabes({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...spielerEntity,
      ...values,
      getraenks: mapIdList(values.getraenks),
      aufgabes: mapIdList(values.aufgabes),
      spiel: spiels.find(it => it.id.toString() === values.spiel.toString()),
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
          ...spielerEntity,
          spiel: spielerEntity?.spiel?.id,
          getraenks: spielerEntity?.getraenks?.map(e => e.id.toString()),
          aufgabes: spielerEntity?.aufgabes?.map(e => e.id.toString()),
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="drinkGameApp.spieler.home.createOrEditLabel" data-cy="SpielerCreateUpdateHeading">
            Create or edit a Spieler
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? <ValidatedField name="id" required readOnly id="spieler-id" label="ID" validate={{ required: true }} /> : null}
              <ValidatedField
                label="Name"
                id="spieler-name"
                name="name"
                data-cy="name"
                type="text"
                validate={{
                  required: { value: true, message: 'This field is required.' },
                }}
              />
              <ValidatedField id="spieler-spiel" name="spiel" data-cy="spiel" label="Spiel" type="select" required>
                <option value="" key="0" />
                {spiels
                  ? spiels.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <FormText>This field is required.</FormText>
              <ValidatedField label="Getraenk" id="spieler-getraenk" data-cy="getraenk" type="select" multiple name="getraenks">
                <option value="" key="0" />
                {getraenks
                  ? getraenks.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField label="Aufgabe" id="spieler-aufgabe" data-cy="aufgabe" type="select" multiple name="aufgabes">
                <option value="" key="0" />
                {aufgabes
                  ? aufgabes.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/spieler" replace color="info">
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

export default SpielerUpdate;
