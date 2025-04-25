"use client"

import { useRef, useState, useEffect } from 'react'
import { Button } from 'primereact/button'
import { Calendar } from 'primereact/calendar'
import { Toast } from 'primereact/toast'
import DayCard from '../DayCard'
import Loading from '../Loading'
import styles from '../page.module.css'

export default function Programs() {
    const rootPath = process.env.NEXT_PUBLIC_API_URL
    const [date, setDate] = useState(new Date())
    const [month, setMonth] = useState(new Date())
    const [loadingVisible, setLoadingVisible] = useState(false)
    const [daysOn, setDaysOn] = useState([])
    const [daysOff, setDaysOff] = useState([])
    const toast = useRef(null)

    useEffect(() => {
        month && getMonthDays()
    }, [month])

    function setMonthAndDate(e) {
        setMonth(e.value)
        setDate(e.value)
    }

    async function getMonthDays() {
        setLoadingVisible(true)

        const formData = new FormData()
        formData.append('date', date.toISOString().split('T')[0])
        const queryString = new URLSearchParams(formData).toString()

        const response = await fetch(rootPath + '/getMonthDays' + `?${queryString}`)

        if (response.ok) {
            const result = await response.json()
            console.log(result)
            const turnOnDays = []
            const turnOffDays = []
            result.forEach(item => {
                if (item.switchValue === true) turnOnDays.push(Number(item.date.slice(-2)))
                if (item.switchValue === false) turnOffDays.push(Number(item.date.slice(-2)))
            })
            setDaysOn(turnOnDays)
            setDaysOff(turnOffDays)
            setLoadingVisible(false)
        } else {
            console.error(response)
            setLoadingVisible(false)
            toast.current.show({ severity: 'error', summary: 'Error', detail: 'Something went wrong' })
        }
    }

    async function setDay(bool) {
        setLoadingVisible(true)

        const formData = new FormData()
        formData.append('date', date.toISOString().split('T')[0])
        formData.append('switchValue', bool)
        const queryString = new URLSearchParams(formData).toString()

        const response = await fetch(rootPath + '/setDay' + `?${queryString}`, {
            method: 'POST'
        })

        if (response.ok) {
            const data = await response.json()
            console.log(data.message)
            setLoadingVisible(false)

            if (data.message === 'Something went wrong') {
                toast.current.show({ severity: 'error', summary: 'Error', detail: data.message })
            } else {
                setMonth(new Date(date))
                toast.current.show({ severity: 'success', summary: 'Success', detail: data.message })
            }
        } else {
            console.error(response)
            setLoadingVisible(false)

            toast.current.show({ severity: 'error', summary: 'Error', detail: 'Something went wrong' })
        }
    }

    const dateTemplate = date => {
        if (daysOn.includes(date.day)) {
            return <span style={{ textDecoration: 'solid underline lime 6px' }}>{date.day}</span>
        }

        if (daysOff.includes(date.day)) {
            return <span style={{ textDecoration: 'solid underline red 6px' }}>{date.day}</span>
        }

        return date.day
    }

    return (
        <>
            <Loading state={loadingVisible} />
            <div className={styles.centeredColumn}>
                <Toast ref={toast} position="bottom-center" />
                <div className="card flex justify-content-center">
                    <Calendar value={month} onChange={(e) => setMonthAndDate(e)} view="month" dateFormat="mm/yy" />
                </div>
                <div className="card flex justify-content-center">
                    <Calendar className="no-month-view" value={date} onChange={(e) => setDate(e.value)} dateTemplate={dateTemplate} inline />
                </div>
                <div className={styles.centeredRow}>
                    <Button icon="pi pi-play" iconPos='right' severity="info" rounded outlined onClick={() => setDay(true)} />
                    <Button icon="pi pi-stop" iconPos='left' severity="info" rounded outlined onClick={() => setDay(false)} />
                </div>
            </div>
        </>)
}
