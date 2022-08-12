import React from 'react';

const HitsItem = ({url, title, description}) => {
    return (
    <li className="hits-item">
        <a href={url} className="title">{title}</a>
        <span className="description">{description}</span>
    </li>
    );
};

export default HitsItem;