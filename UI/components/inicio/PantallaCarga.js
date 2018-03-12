import React from 'react';
import { View, Text, StyleSheet } from 'react-native';
import Screen from './../elementos/Screen';

const styles = StyleSheet.create({
  container: {
    alignItems: "center",
    justifyContent: "center",
    flex: 1
  }
});

class PantallaCarga extends React.Component {
  constructor(props) {
    super(props);
    this.cargarDatos();
  }

  cargarDatos = async () => {
    const jsonFirebase = {User: "KaoPian"}
    this.props.navigation.navigate('InitStack');
  }

  render() {
    return (
      <Screen>
        <Text>
          Cargando...
        </Text>
      </Screen>
    );
  }
}

export default PantallaCarga;
