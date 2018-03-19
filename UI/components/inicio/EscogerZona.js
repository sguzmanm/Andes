import React from 'react';
import { View, Text, StyleSheet, TouchableOpacity } from 'react-native';
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
      <Screen style={styles.container}>
        <TouchableOpacity>
          <Button
            title="Uniandes"
            backgroundColor="#fdeff9"
            color="#7303c0"
            onPress={() => this.props.navigation.navigate('AppNav')}
            raised
          />
        </TouchableOpacity>
        <View style={{height:16}}/>
        <TouchableOpacity>
          <Button
            title="Externado"
            backgroundColor="#fdeff9"
            color="#7303c0"
            onPress={() => {}}
            raised
          />
        </TouchableOpacity>
        <View style={{height:16}}/>
        <TouchableOpacity>
          <Button
            title="Javeriana"
            backgroundColor="#fdeff9"
            color="#7303c0"
            onPress={() => {}}
            raised
          />
        </TouchableOpacity>
      </Screen>
    );
  }
}

export default EscogerZona;
