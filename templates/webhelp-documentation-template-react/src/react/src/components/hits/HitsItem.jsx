import React from 'react';

/**
 * Class that renders an hit in the hits list.
 * @param {*} url is the hit's url represented by a String.
 * @param {*} title is the hit's title represented by a String.
 * @param {*} description is the hit's description represented by a String
 * @param {*} documentation is the hit's documentation represented by a String.
 * @param {*} breadcrub is the hit's breadcrumb represented by an object with one single key:value where key is the category's title and key is the url to the category.
 * @returns an item in the list.
 */
const HitsItem = ({ url, title, description, documentation, breadcrumb }) => {
    return (
        <li className="hits-item">
            <span className="documentation">{documentation}</span>
            <a href={url} className="title">{title}</a>
            <span class="breadcrumb">
                {breadcrumb !== undefined ? breadcrumb.map((level) => {
                    // Check if the category is the last in the breadcrumb in order to not render an '>' character.
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