import React from 'react';

const HitsList = ({items}) => {
    if(items?.length > 0)
        return (<div className="row"><ul className="hits">{items.map(item => item)}</ul></div>);
    else
        return (<div className="error"><strong>No results found!</strong></div>);
}

export default HitsList;