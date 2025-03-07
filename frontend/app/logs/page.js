"use client"

import { useState } from 'react'
import styles from '../page.module.css'

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
        fontFamily: "'Courier New', Courier, monospace",
        whiteSpace: "pre",
        overflowX: "auto"
    }

    return (
        <div className={styles.centeredColumn}>
            <div className="card">
                <div className="card-body" style={customStyles}>
                    {text}
                </div>
            </div>
        </div>
    )
}
