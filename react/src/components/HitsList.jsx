import React from 'react';

const HitsList = ({items}) => {
    return (
        <ul className="hits">
            {items?.length > 0 ? (items.map(item => item)) : <h2 className="error">No results found!</h2>}
        </ul>
    )
}

export default HitsList;