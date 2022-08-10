import React from 'react';
import HitTitle from "./HitTitle";
import HitDescription from "./HitDescription";

const HitsItem = (props) => {
    return <li className="hits-item">
                <HitTitle url={props.url} title={props.title}></HitTitle>
                <HitDescription description={props.description}></HitDescription>
            </li>;
};

export default HitsItem;