import React from 'react';

const HitsItem = ({ url, title, description, documentation, breadcrumb }) => {
    return (
        <li className="hits-item">
            <span class="breadcrumb">{breadcrumb !== undefined ? breadcrumb.split('>').map((level) => { return (<span className="breadcrumb-element">{level}</span>)}) : null}</span>
            <a href={url} className="title">{title}</a>
            <span className="documentation">{documentation}</span>
            <span className="description">{description}</span>
        </li>
    );
};

export default HitsItem;