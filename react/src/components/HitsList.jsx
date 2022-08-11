import React from 'react';

const HitsList = ({items}) => {
    if(items?.length > 0)
        return (<ul className="hits">{items.map(item => item)}</ul>);
    else
        return (<h2 className="error">No results found!</h2>);
}

export default HitsList;