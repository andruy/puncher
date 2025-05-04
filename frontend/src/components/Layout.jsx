import { Outlet } from 'react-router-dom'
import MenuButton from './MenuButton'

const Layout = () => (
    <>
        <MenuButton />
        <Outlet />
    </>
)

export default Layout
