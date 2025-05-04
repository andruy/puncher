import { useRef, useEffect } from "react"

export default function Loading({ state }) {
    const ref = useRef(null)

    useEffect(() => {
        if (ref.current) {
            ref.current.style.display = state ? 'block' : 'none';
        }
    }, [state])

    return (
        <div ref={ref} className="loading">
            <div>
                <div className="c1"></div>
                <div className="c2"></div>
                <div className="c3"></div>
                <div className="c4"></div>
            </div>
            <span>Loading...</span>
        </div>
    )
}
