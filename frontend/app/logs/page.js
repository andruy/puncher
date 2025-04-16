"use client"

import { useState } from 'react'
import { Button } from 'primereact/button'

export default function Logs() {
    const [text, setText] = useState('Did not find any logs')

    async function send() {
        const response = await fetch('/logReader')

        if (response.ok) {
            const result = await response.json()
            setText(result.logs)
            return result
        } else {
            return "Something went wrong"
        }
    }

    const customStyles = {
        display: "flex",
        alignItems: "center",
        maxWidth: "95%",
        maxHeight: "70vh",
        overflow: "auto",
        whiteSpace: "pre"
    }

    return (
        <div style={{ display: "flex", flexDirection: "column", alignItems: "center", width: "100%" }}>
            <div style={customStyles}>
                <p style={{ fontFamily: "'Courier New', Courier, monospace" }}>
                    {text}
                </p>
            </div>
            <Button icon="pi pi-refresh" onClick={send} style={{ marginTop: "1rem" }} />
        </div>
    )
}
