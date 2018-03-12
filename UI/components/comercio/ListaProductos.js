import React from 'react';
import { View, Text, StyleSheet, FlatList } from 'react-native';
import { Button, Icon } from 'react-native-elements';
import Header from './../elementos/Header';
import Screen from './../elementos/Screen';
import Producto from './Producto';

class ListaProductos extends React.Component {

  cosasPrueba = [
    {
      id: "k4kdf9546",
      nombre: "Gomitas a 100!",
      precio: 200,
      descripcion: "Lleve sus gomas!! Solo en el Ml 5!"
    },
    {
      id: "eqw6k89gh9x",
      nombre: "Earpods baratos",
      precio: 9000,
      descripcion: "Recién llegados de China, lléve sus earpods de diferentes colores"
    },
    {
      id:"kl84j3vbcxz8",
      nombre: "Nivel 12 APO 2",
      precio: 80000,
      descripcion: "Completo y comentado! 5 seguro y repaso seguro para el final"
    }
  ];

  render() {
    return(
      <Screen>
        <Header
          leftIcon="menu"
          leftIconAction={() => this.props.navigation.navigate("DrawerToggle")}
          title="Lista de productos"
          rightIcon="info"
        />
        <FlatList 
          data={this.cosasPrueba}
          keyExtractor={elemento => elemento.id}
          style={{ paddingHorizontal: 24 }}
          ItemSeparatorComponent={() => <View style={{ height: 2, width: '100%', backgroundColor: 'rgba(255, 255, 255, 0.2)' }} />}
          renderItem={( item ) => {return(
            <Producto
              producto={item}
            />
          );
          }}
        />
      </Screen>
    );
  }
}

export default ListaProductos;