import React from 'react';
import { StyleSheet, Text, View } from 'react-native';
import { StackNavigator, DrawerNavigator, SwitchNavigator } from 'react-navigation';
import PantallaCarga from './components/inicio/PantallaCarga';
import EscogerZona from './components/inicio/EscogerZona';
import MapaPrincipal from './components/navegacion/MapaPrincipal';
import Buscar from './components/navegacion/busqueda/Buscar';
import ListaProductos from './components/comercio/ListaProductos';

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#fff',
    alignItems: 'center',
    justifyContent: 'center',
  },
});

const StackNavegacion = StackNavigator({
  MapaPrincipal: {
    screen: MapaPrincipal
  },
  Buscar: {
    screen: Buscar
  }
}, {
  headerMode: 'none'
});

const AppNav = DrawerNavigator({
  StackNavegacion: {
    screen: StackNavegacion
  },
  ListaProductos: {
    screen: ListaProductos
  }
});

const InitStack = StackNavigator({
  EscogerZona: {
    screen: EscogerZona
  },
}, {
  headerMode: 'none'
});

const SwitchFlow = SwitchNavigator(
  {
    AppNav: AppNav,
    PantallaCarga: PantallaCarga,
    InitStack: InitStack
  },
  {
    initialRouteName: 'PantallaCarga'
  }
);

export default class App extends React.Component {
  render() {
    return (
      <SwitchFlow />
    );
  }
}
