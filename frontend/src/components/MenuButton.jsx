import { SpeedDial } from 'primereact/speeddial'
import { useNavigate } from 'react-router-dom'

export default function MenuButton() {
    const navigate = useNavigate()

    const items = [
        {
            label: 'Home',
            icon: 'pi pi-home',
            command: () => navigate('/')
        },
        {
            label: 'Features',
            icon: 'pi pi-clock',
            command: () => navigate('/features')
        },
        {
            label: 'Programs',
            icon: 'pi pi-calendar-clock',
            command: () => navigate('/programs')
        },
        {
            label: 'Logs',
            icon: 'pi pi-server',
            command: () => navigate('/logs')
        }
    ]

    return (
        <div className='card'>
            <SpeedDial model={items} direction="right" showIcon="pi pi-bars" hideIcon="pi pi-times" buttonClassName="p-button-outlined" style={{ left: 20, top: 20 }} />
        </div>
    )
}
