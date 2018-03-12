import React from 'react';
import { View, Text, StyleSheet } from 'react-native';
import { Button } from 'react-native-elements';
import Screen from './../elementos/Screen';

const styles = StyleSheet.create({
  container: {
    alignItems: "center",
    justifyContent: "center",
    flex: 1
  }
});

class EscogerZona extends React.Component{
  render(){
    return (
      <Screen>
        <Button
          title="Uniandes"
          onPress={() => this.props.navigation.navigate('AppNav')}
          raised
        />
        <Button
          title="Externado"
          raised
        />
      </Screen>
    );
  }
}

export default EscogerZona;
