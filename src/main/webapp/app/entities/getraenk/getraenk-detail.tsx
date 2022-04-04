import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './getraenk.reducer';

export const GetraenkDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const getraenkEntity = useAppSelector(state => state.getraenk.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="getraenkDetailsHeading">Getraenk</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{getraenkEntity.id}</dd>
          <dt>
            <span id="name">Name</span>
          </dt>
          <dd>{getraenkEntity.name}</dd>
          <dt>
            <span id="groesse">Groesse</span>
          </dt>
          <dd>{getraenkEntity.groesse}</dd>
        </dl>
        <Button tag={Link} to="/getraenk" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/getraenk/${getraenkEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default GetraenkDetail;
