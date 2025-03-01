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
            icon: 'pi pi-star',
            command: () => router.push('/features')
        },
        {
            label: 'About',
            icon: 'pi pi-info',
            command: () => router.push('/about')
        }
    ]

    return (
        <div className='card'>
            <SpeedDial model={items} direction="down" showIcon="pi pi-bars" hideIcon="pi pi-times" buttonClassName="p-button-outlined" style={{ left: 20, top: 20 }} />
        </div>
    )
}
