"use client"

import { SpeedDial } from 'primereact/speeddial';
import { useRouter } from 'next/navigation';

export default function MenuButton() {
    const router = useRouter()
    const items = [
        {
            label: 'Home',
            icon: 'pi pi-home',
            command: () => router.push('/')
        },
        {
            label: 'Features',
            icon: 'pi pi-clock',
            command: () => router.push('/features')
        },
        {
            label: 'Programs',
            icon: 'pi pi-calendar-clock',
            command: () => router.push('/programs')
        },
        {
            label: 'Logs',
            icon: 'pi pi-server',
            command: () => router.push('/logs')
        }
    ]

    return (
        <div className='card'>
            <SpeedDial model={items} direction="right" showIcon="pi pi-bars" hideIcon="pi pi-times" buttonClassName="p-button-outlined" style={{ left: 20, top: 20 }} />
        </div>
    )
}
