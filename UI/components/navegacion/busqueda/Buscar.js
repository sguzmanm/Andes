import React from 'react';
import { View, Text, StyleSheet } from 'react-native';
import { Button, Icon } from 'react-native-elements';
import Header from './../../elementos/Header';
import Screen from './../../elementos/Screen';

class Buscar extends React.Component {
  render() {
    return(
      <Screen>
        <Header
          leftIcon="keyboard-arrow-left"
          leftIconAction={() => this.props.navigation.goBack()}
          title="Buscar"
          rightIcon="info"
        />
      </Screen>
    );
  }
}

export default Buscar;