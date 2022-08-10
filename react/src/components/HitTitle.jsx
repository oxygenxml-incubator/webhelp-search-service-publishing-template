import React from 'react'

const HitTitle = ({url, title}) => {
    return <a href={url} className="title">{title}</a>
};

export default HitTitle;