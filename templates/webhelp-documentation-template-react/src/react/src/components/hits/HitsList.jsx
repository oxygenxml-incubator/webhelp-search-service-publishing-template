import React from 'react';
import HitsItem from './HitsItem.jsx';

/**
 * Class that renders a list of hits.
 * @param {*} hits is the hits Array returned in Algolia response when performing a search.
 * @returns a list of items.
 */
const HitsList = ({ hits }) => {
    if (hits?.length > 0) {
        return (<div className="hits-container"><ul className="hits">{
            hits.map((hit) => {
                return (
                    <HitsItem
                        key={"objectID" in hit ? hit.objectID : hit.toString()}
                        title={hit.title}
                        description={hit.shortDescription}
                        url={hit.objectID}
                        documentation={hit.documentation}
                        breadcrumb={hit.breadcrumb}
                    />
                );
            })
        }</ul></div>);
    }
    else
        return (<div className="no-results"><strong>No results found!</strong></div>);
}

export default HitsList;