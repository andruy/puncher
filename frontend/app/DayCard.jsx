"use client"

import { useState, useEffect } from 'react'
import { Checkbox } from "primereact/checkbox"
import styles from './page.module.css'

export default function DayCard({ programId, monday, tuesday, wednesday, thursday, friday, setNewProgram }) {
    const [mondayChecked, setMondayChecked] = useState(monday)
    const [tuesdayChecked, setTuesdayChecked] = useState(tuesday)
    const [wednesdayChecked, setWednesdayChecked] = useState(wednesday)
    const [thursdayChecked, setThursdayChecked] = useState(thursday)
    const [fridayChecked, setFridayChecked] = useState(friday)

    useEffect(() => {
        setNewProgram(
            {
                "id": programId,
                "dayFlags": [
                    {
                        "day": 1,
                        "isOn": mondayChecked
                    },
                    {
                        "day": 2,
                        "isOn": tuesdayChecked
                    },
                    {
                        "day": 3,
                        "isOn": wednesdayChecked
                    },
                    {
                        "day": 4,
                        "isOn": thursdayChecked
                    },
                    {
                        "day": 5,
                        "isOn": fridayChecked
                    }
                ]
            }
        )
    }, [mondayChecked, tuesdayChecked, wednesdayChecked, thursdayChecked, fridayChecked])

    return (
        <div className={styles.centeredColumnSmallGap} style={{ border: '1px solid #424b57', borderRadius: '6px', width: '100%' }}>
            <div style={{ display: 'flex', alignItems: 'center' }}>
                <Checkbox onChange={e => setMondayChecked(e.checked)} checked={mondayChecked}></Checkbox>
                <h4 style={{ marginLeft: '.5rem' }}>Monday</h4>
            </div>
            <div style={{ display: 'flex', alignItems: 'center' }}>
                <Checkbox onChange={e => setTuesdayChecked(e.checked)} checked={tuesdayChecked}></Checkbox>
                <h4 style={{ marginLeft: '.5rem' }}>Tuesday</h4>
            </div>
            <div style={{ display: 'flex', alignItems: 'center' }}>
                <Checkbox onChange={e => setWednesdayChecked(e.checked)} checked={wednesdayChecked}></Checkbox>
                <h4 style={{ marginLeft: '.5rem' }}>Wednesday</h4>
            </div>
            <div style={{ display: 'flex', alignItems: 'center' }}>
                <Checkbox onChange={e => setThursdayChecked(e.checked)} checked={thursdayChecked}></Checkbox>
                <h4 style={{ marginLeft: '.5rem' }}>Thursday</h4>
            </div>
            <div style={{ display: 'flex', alignItems: 'center' }}>
                <Checkbox onChange={e => setFridayChecked(e.checked)} checked={fridayChecked}></Checkbox>
                <h4 style={{ marginLeft: '.5rem' }}>Friday</h4>
            </div>
        </div>
    )
}
