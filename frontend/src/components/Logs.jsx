import { useState, useEffect } from 'react'
import { Button } from 'primereact/button'

export default function Logs() {
	const rootPath = ""

    useEffect(() => {
        send()
    }, [])

    const [text, setText] = useState('Did not find any logs')

    async function send() {
        const response = await fetch(rootPath + '/logReader')

        if (response.ok) {
            const result = await response.json()
            setText(result.logs)
            return result
        } else {
            return setText("Something went wrong")
        }
    }

    const customStyles = {
        fontFamily: "'Courier New', Courier, monospace",
        overflowX: "auto",
        whiteSpace: "pre",
        maxWidth: "95%",
        marginTop: "6.5rem"
    }

    return (
        <>
            <p style={customStyles}>
                {text}
            </p>
            <Button icon="pi pi-refresh" onClick={() => send()} style={{ marginTop: "1rem", marginBottom: "2rem" }} />
        </>
    )
}
