import React from 'react';

const HitsItem = ({ url, title, description, documentation, breadcrumb }) => {
    return (
        <li className="hits-item">
            <span className="documentation">{documentation}</span>
            <a href={url} className="title">{title}</a>
            <span class="breadcrumb">
                {breadcrumb !== undefined ? breadcrumb.map((level) => {
                    if (breadcrumb[breadcrumb.length - 1] === level)
                        return (
                            <a href={level[Object.keys(level)[0]]}>
                                <span className="breadcrumb-element">{Object.keys(level)[0]}</span>
                            </a>)
                    else
                        return (
                            <a href={level[Object.keys(level)[0]]}>
                                <span className="breadcrumb-element">{Object.keys(level)[0] + ' > '}</span>
                            </a>)
                }) : null}
            </span>
            <span className="description">{description}</span>
        </li>
    );
};

export default HitsItem;