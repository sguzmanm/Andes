import React from 'react';
import { View, Text, StyleSheet } from 'react-native';
import { Button, Icon } from 'react-native-elements';
import Header from './../elementos/Header';
import Screen from './../elementos/Screen';
//Import de componente nativo
import MapView from './../nativo/MapView';

const styles = StyleSheet.create({
  container: {
    alignItems: "center",
    justifyContent: "center",
    flex: 1
  },
  search: {
    position: 'absolute',
    right: 24,
    bottom: 24,
    height: 64,
    width: 64,
    shadowColor: '#000',
    shadowOffset: {
      width: 0,
      height: 2
    },
    shadowRadius: 4,
    shadowOpacity: 0.25
  },
  map: {
    flex: 1,
    alignItems: "center",
    justifyContent: "center"
  }
});

class MapaPrincipal extends React.Component {

  static navigationOptions = {
    drawerLabel: 'Navegación',
    drawerIcon: ({ tintColor }) => <Icon name="directions-run" color={tintColor} />
  };

  render() {
    const { params } = this.props.navigation.state;
    let destino = undefined;
    if(params)
      destino = params.destino
    return(
      <Screen>
        <Header
          leftIcon="menu"
          leftIconAction={() => this.props.navigation.navigate('DrawerToggle')}
          title="Navegación"
        />
        <Icon
          name="search"
          containerStyle={styles.search}
          reverse
          color="#852D91"
          size={32}
          onPress={() => this.props.navigation.navigate("Buscar")}
        />
        {destino ?
          <View>
            <Text>Me estoy dirigiendo hacia: {destino.nombre}</Text>
            <Text>Que queda en: [{destino.ubicacion.latitud}, {destino.ubicacion.longitud}]</Text>
            <Text>En el piso: {destino.ubicacion.piso}</Text> 
          </View>
          :<View></View>}
          <MapView style={styles.map}/>
      </Screen>
    );
  }
}

export default MapaPrincipal;