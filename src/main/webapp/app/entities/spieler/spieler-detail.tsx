import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './spieler.reducer';

export const SpielerDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const spielerEntity = useAppSelector(state => state.spieler.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="spielerDetailsHeading">Spieler</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{spielerEntity.id}</dd>
          <dt>
            <span id="name">Name</span>
          </dt>
          <dd>{spielerEntity.name}</dd>
          <dt>Spiel</dt>
          <dd>{spielerEntity.spiel ? spielerEntity.spiel.id : ''}</dd>
          <dt>Getraenk</dt>
          <dd>
            {spielerEntity.getraenks
              ? spielerEntity.getraenks.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.id}</a>
                    {spielerEntity.getraenks && i === spielerEntity.getraenks.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
          <dt>Aufgabe</dt>
          <dd>
            {spielerEntity.aufgabes
              ? spielerEntity.aufgabes.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.id}</a>
                    {spielerEntity.aufgabes && i === spielerEntity.aufgabes.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
        </dl>
        <Button tag={Link} to="/spieler" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/spieler/${spielerEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default SpielerDetail;
