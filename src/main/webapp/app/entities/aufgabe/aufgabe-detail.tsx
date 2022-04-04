import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './aufgabe.reducer';

export const AufgabeDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const aufgabeEntity = useAppSelector(state => state.aufgabe.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="aufgabeDetailsHeading">Aufgabe</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{aufgabeEntity.id}</dd>
          <dt>
            <span id="name">Name</span>
          </dt>
          <dd>{aufgabeEntity.name}</dd>
          <dt>
            <span id="kurztext">Kurztext</span>
          </dt>
          <dd>{aufgabeEntity.kurztext}</dd>
          <dt>
            <span id="beschreibung">Beschreibung</span>
          </dt>
          <dd>{aufgabeEntity.beschreibung}</dd>
          <dt>
            <span id="kategorie">Kategorie</span>
          </dt>
          <dd>{aufgabeEntity.kategorie}</dd>
          <dt>
            <span id="level">Level</span>
          </dt>
          <dd>{aufgabeEntity.level}</dd>
        </dl>
        <Button tag={Link} to="/aufgabe" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/aufgabe/${aufgabeEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default AufgabeDetail;
