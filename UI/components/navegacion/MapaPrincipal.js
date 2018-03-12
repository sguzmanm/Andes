import React from 'react';
import { View, Text, StyleSheet } from 'react-native';
import { Button, Icon } from 'react-native-elements';
import Header from './../elementos/Header';
import Screen from './../elementos/Screen';

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
  }
});

class MapaPrincipal extends React.Component {
  constructor(){
    super();
    this.state = {
      puntoSeleccionado : {
        Latitud: null,
        Longitud: null
      }
    };
  }

  render() {
    return(
      <Screen>
        <Header
          leftIcon="menu"
          leftIconAction={() => this.props.navigation.navigate('DrawerToggle')}
          title="Menú principal"
          rightIcon="info"
          rightIconAction={() => this.setState({ puntoSeleccionado: { Latitud: 2, Longitud: 1} })}
        />
        <Icon
          name="search"
          containerStyle={styles.search}
          reverse
          color="#852D91"
          size={32}
          onPress={() => this.props.navigation.navigate("Buscar")}
        />
      </Screen>
    );
  }
}

export default MapaPrincipal;