import { SpeedDial } from 'primereact/speeddial'
import { useNavigate } from "react-router-dom"

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
            icon: 'pi pi-star',
            command: () => navigate('/features')
        },
        {
            label: 'Program',
            icon: 'pi pi-info',
            command: () => navigate('/program')
        }
    ]

    return (
        <div className='card'>
            <SpeedDial model={items} direction="down" showIcon="pi pi-bars" hideIcon="pi pi-times" buttonClassName="p-button-outlined" style={{ left: 20, top: 20 }} />
        </div>
    )
}
