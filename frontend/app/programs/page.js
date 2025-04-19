"use client"

import { useRef, useState, useEffect } from 'react'
import { Stepper } from 'primereact/stepper'
import { StepperPanel } from 'primereact/stepperpanel'
import { Button } from 'primereact/button'
import { Calendar } from 'primereact/calendar'
import { Toast } from 'primereact/toast'
import DayCard from '../DayCard'
import styles from '../page.module.css'

export default function Programs() {
	const rootPath = process.env.NEXT_PUBLIC_API_URL
    const stepperRef = useRef(null)
    const [date, setDate] = useState(null)
    const [weekNumber, setWeekNumber] = useState('?')
    const [booleans, setBooleans] = useState([])
    const [program, setProgram] = useState({})
    const [newProgram, setNewProgram] = useState({})
    const [weekButtonIsDisabled, setWeekButtonIsDisabled] = useState(true)
    const [sendButtonIsDisabled, setSendButtonIsDisabled] = useState(false)
    const toast = useRef(null)

    useEffect(() => {
        date && send(date)
    }, [date])

    async function send(theDate) {
        const formData = new FormData()
        formData.append('date', theDate.toISOString().split('T')[0])
        const queryString = new URLSearchParams(formData).toString()

        !weekButtonIsDisabled && setWeekButtonIsDisabled(true)
        setWeekNumber('?')
        const response = await fetch(rootPath + '/forDay' + `?${queryString}`)

        if (response.ok) {
            const result = await response.json()
            console.log(result)
            setWeekNumber(result.id.toString().slice(-2))
            setProgram(result)
            setWeekButtonIsDisabled(false)
            const bools = []
            result.dayFlags.forEach(dayFlag => {
                bools.push(dayFlag.isOn)
            })
            setBooleans(bools)
            return { report: "Got week #" + result.id }
        } else {
            console.error(response)
            setWeekButtonIsDisabled(true)
            setWeekNumber('0')
            setProgram({})
            return "Something went wrong"
        }
    }

    async function sendNewProgram() {
        setSendButtonIsDisabled(true)
        const response = await fetch(rootPath + '/setWeekProgram', {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(newProgram)
        })

        if (response.ok) {
            setSendButtonIsDisabled(false)
            const result = await response.json()
            console.log(result)
            toast.current.show({ severity: 'success', summary: 'Success', detail: 'Set program for ' + newProgram.id })
        } else {
            setSendButtonIsDisabled(false)
            console.error(response)
            console.log(newProgram)
            toast.current.show({ severity: 'error', summary: 'Error', detail: 'Something went wrong' })
            return "Something went wrong"
        }
    }

    function refreshOnPrevCallback() {
        stepperRef.current.prevCallback()
        send(date)
    }

    function checkAllWeeksOfMonth() {
        const firstDayOfMonth = new Date(date.getFullYear(), date.getMonth(), 1) // Get the first day of the month [1.2.2].
        const dayOfWeek = firstDayOfMonth.getDay() // Get the day of the week (0-6, Sunday is 0) [1.7.2, 1.5.3].

        if (dayOfWeek === 0 || dayOfWeek === 6) {
            // If the first day of the month is Sunday or Saturday, set it to Monday [1.5.3].
            firstDayOfMonth.setDate(firstDayOfMonth.getDate() + (dayOfWeek === 0 ? 1 : 2))
        }

        // firstDayOfMonth.get

        const diff = (8 - dayOfWeek) % 7 // Calculate the difference in days to reach the first Monday [1.8.2].

        const firstMonday = new Date(firstDayOfMonth.getFullYear(), firstDayOfMonth.getMonth(), 1 + diff) // Construct a new Date object for the first Monday [1.2.2, 1.8.2].
    }

    return (
        <div className="card flex justify-content-center">
            <Toast ref={toast} position="bottom-center" />
            <Stepper linear ref={stepperRef} style={{ flexBasis: '50rem' }}>
                <StepperPanel header="Select date">
                    <div className={styles.centeredColumn}>
                        <div className="card flex justify-content-center">
                            <Calendar value={date} onChange={(e) => setDate(e.value)} inline />
                        </div>
                        <Button label={'Week ' + weekNumber} icon="pi pi-arrow-right" iconPos="right" onClick={() => stepperRef.current.nextCallback()} disabled={weekButtonIsDisabled} />
                    </div>
                </StepperPanel>
                <StepperPanel header="Assign program">
                    <div className={styles.centeredColumn}>
                        <DayCard
                            programId={program.id}
                            monday={booleans[0]}
                            tuesday={booleans[1]}
                            wednesday={booleans[2]}
                            thursday={booleans[3]}
                            friday={booleans[4]}
                            setNewProgram={setNewProgram}
                        />
                        <div className={styles.spreadApart}>
                            <Button label="Back" severity="secondary" icon="pi pi-arrow-left" onClick={refreshOnPrevCallback} />
                            <Button label="Send" icon="pi pi-send" iconPos="right" onClick={sendNewProgram} disabled={sendButtonIsDisabled} />
                        </div>
                    </div>
                </StepperPanel>
            </Stepper>
        </div>
    )
}
